import {
  CoffeeCountryMatcherToKor,
  CoffeeCategoryMatcherToKor,
} from '@/utils/CoffeeMatcher';
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';
import 'swiper/css/pagination';
import { Pagination } from 'swiper';
import Link from 'next/link';
import Image from 'next/image';
import { useState, useEffect } from 'react';

export default function SimilarCoffee({ similarCoffee }: any) {
  const [coffeeInfo, setCoffeeInfo] = useState<any>();
  useEffect(() => {
    if (similarCoffee !== undefined) setCoffeeInfo(similarCoffee);
  }, [similarCoffee]);

  return (
    <div className="w-full ">
      <div className="mx-3 mt-6 text-base font-semibold lg:text-xl">
        <span className="text-y-brown mr-1">비슷한</span>
        <span className="text-black">맥주</span>
      </div>
      <Swiper
        className="w-full h-fit"
        slidesPerView={2.4}
        spaceBetween={20}
        modules={[Pagination]}
      >
        {coffeeInfo?.map((el: any, idx: number) => (
          <SwiperSlide key={idx}>
            <Link href={`/coffee/${el.coffeeId}`}>
              <div className="rounded-2xl w-full m-2 border bg-white text-y-black drop-shadow-lg text-xs overflow-hidden">
                <div
                  className={`${
                    idx % 2 === 0 ? 'bg-y-cream' : 'bg-y-lemon'
                  } p-4 rounded-t-2xl`}
                >
                  <div className="text-base font-semibold truncate">
                    {el?.korName}
                  </div>
                  <div className="truncate">
                    {el?.coffeeCategories === null ||
                    el?.coffeeCategories.length === 0 ? (
                      <></>
                    ) : (
                      <span>
                        {el?.coffeeCategories?.map(
                          (category: any, idx: number) => (
                            <span key={idx}>
                              {CoffeeCategoryMatcherToKor(
                                category?.coffeeCategoryType
                              )}
                              /
                            </span>
                          )
                        )}
                      </span>
                    )}
                    {el?.country === null ? (
                      <></>
                    ) : (
                      <span>{CoffeeCountryMatcherToKor(el?.country)}/</span>
                    )}
                    {el?.roasting === null ? <></> : <span>{el?.roasting}%</span>}
                    {el?.acidity === null ? <></> : <span>/{el?.acidity}Acidity</span>}
                  </div>
                </div>
                <Image
                  className="pt-3 rounded-2xl m-auto select-none"
                  alt="Coffee"
                  src={el?.thumbnail}
                  width={300}
                  height={300}
                  priority
                />
              </div>
            </Link>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
}
