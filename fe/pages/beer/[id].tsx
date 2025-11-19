import Head from 'next/head';
import Image from 'next/image';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Pagination } from 'swiper';
import 'swiper/css';
import 'swiper/css/pagination';
import axios from '@/pages/api/axios';
import SmallRatingCard from '@/components/smallCards/SmallRatingCard';
import SmallPairingCard from '@/components/smallCards/SmallPairingCard';
import SimilarBeer from '@/components/smallCards/SimilarBeer';
import RatingTitle from '@/components/beerPage/RatingTitle';
import PairingTitle from '@/components/beerPage/PairingTitle';
import BeerDetailCard from '@/components/beerPage/BeerDetailCard';
import { useRouter } from 'next/router';
import { useRecoilValue } from 'recoil';
import { userId } from '@/atoms/login';
import { GetServerSideProps, InferGetServerSidePropsType } from 'next';
import {
  BeerInfo,
  RatingInfo,
  PairingInfo,
  SimilarBeerProps,
  RatingCardProps,
  PairingCardProps,
} from '@/components/beerPage/BeerDeclare';

export default function Beer({
  beerInfo,
  ratingInfo,
  pairingInfo,
  similarBeer,
}: InferGetServerSidePropsType<typeof getServerSideProps>) {
  const router = useRouter();
  const beerId = Number(router.query.id);
  const USERID: number = useRecoilValue(userId);

  // props로 받은 평가 목록에서 사용자의 평가가 있는지 확인
  const myRating = ratingInfo?.data?.find((rating) => rating.userId === USERID);
  const hasRating = !!myRating;
  const myRatingId = myRating?.ratingId;

  return (
    <>
      <Head>
        <title>{beerInfo.beerDetailsBasic.korName} - GetABeer</title>
        <meta name="description" content={beerInfo.beerDetailsBasic.content} />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/images/logo.png" />
      </Head>

      <main className="m-auto h-screen max-w-4xl relative">
        <Image
          className=" w-full h-screen left-0 top-0 fixed -z-10 select-none"
          src="/images/background.png"
          alt="bg"
          width={500}
          height={500}
        />
        <div className="m-3">
          <BeerDetailCard
            cardProps={beerInfo}
            hasRating={hasRating}
            myRatingId={myRatingId}
          />
        </div>
        {/* 평가 */}
        <RatingTitle
          ratingCount={ratingInfo?.pageInfo?.totalElements}
          beerId={beerId}
        />
        <div>
          {ratingInfo === undefined || ratingInfo?.data.length === 0 ? (
            <div className="noneContent text-xs lg:text-sm">
              <Image
                className="m-auto pb-3 opacity-50"
                src="/images/logo.png"
                alt="logo"
                width={40}
                height={40}
              />
              등록된 평가가 없습니다.
            </div>
          ) : (
            <Swiper
              className="w-full h-fit"
              slidesPerView={2.2}
              spaceBetween={10}
              modules={[Pagination]}
            >
              {ratingInfo?.data.map((el: RatingCardProps) => (
                <SwiperSlide key={el?.ratingId}>
                  <SmallRatingCard ratingProps={el} />
                </SwiperSlide>
              ))}
            </Swiper>
          )}
        </div>
        {/* 페어링 */}
        <PairingTitle
          pairngCount={pairingInfo?.pageInfo?.totalElements}
          beerId={beerId}
        />
        <div>
          {pairingInfo === undefined || pairingInfo?.data.length === 0 ? (
            <div className="noneContent text-xs lg:text-sm">
              <Image
                className="m-auto pb-3 opacity-50"
                src="/images/logo.png"
                alt="logo"
                width={40}
                height={40}
              />
              등록된 페어링이 없습니다.
            </div>
          ) : (
            <Swiper
              className="w-full h-fit"
              slidesPerView={2.2}
              spaceBetween={10}
              modules={[Pagination]}
            >
              {pairingInfo?.data.map((el: PairingCardProps) => (
                <SwiperSlide key={el?.pairingId}>
                  <SmallPairingCard pairingProps={el} />
                </SwiperSlide>
              ))}
            </Swiper>
          )}
        </div>
        <SimilarBeer similarBeer={similarBeer} />
        <div className="h-20"></div>
      </main>
    </>
  );
}

export const getServerSideProps: GetServerSideProps<{
  beerInfo: BeerInfo;
  ratingInfo: RatingInfo;
  pairingInfo: PairingInfo;
  similarBeer: SimilarBeerProps[];
}> = async (context) => {
  const { params } = context;
  if (!params || typeof params.id !== 'string') {
    return {
      notFound: true,
    };
  }
  const { id } = params;
  const API_URL = process.env.API_URL;

  // 페이지에 필요한 모든 API 요청을 생성
  const beerInfoReq = axios.get(`${API_URL}/api/beers/${id}`);
  const ratingInfoReq = axios.get(
    `${API_URL}/api/ratings/page/mostlikes?beerId=${id}&page=1&size=5`
  );
  const pairingInfoReq = axios.get(
    `${API_URL}/api/pairings/page/mostlikes/all?beerId=${id}&page=1&size=5`
  );
  const similarBeerReq = axios.get(`${API_URL}/api/beers/${id}/similar`);

  try {
    // Promise.all을 사용하여 모든 API 요청을 병렬로 처리
    const [
      beerInfoRes,
      ratingInfoRes,
      pairingInfoRes,
      similarBeerRes,
    ] = await Promise.all([
      beerInfoReq,
      ratingInfoReq,
      pairingInfoReq,
      similarBeerReq,
    ]);

    // 받아온 데이터를 페이지 컴포넌트의 props로 전달
    return {
      props: {
        beerInfo: beerInfoRes.data,
        ratingInfo: ratingInfoRes.data,
        pairingInfo: pairingInfoRes.data,
        similarBeer: similarBeerRes.data,
      },
    };
  } catch (error) {
    // 에러 발생 시 notFound 페이지를 보여주거나 에러 페이지로 리디렉션
    console.error('Failed to fetch beer page data:', error);
    return {
      notFound: true,
    };
  }
};
