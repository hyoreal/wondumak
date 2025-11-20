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

export default function RecommendCoffee({ recommendCoffee }: any) {
  const [coffeeInfo, setCoffeeInfo] = useState<any>();
  useEffect(() => {
    if (recommendCoffee !== undefined) setCoffeeInfo(recommendCoffee);
  }, [recommendCoffee]);

  return (
    <div className="w-full ">
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
                    {el?.coffeeCategories === null ? (
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
                {el?.thumbnail === null ? (
                  <></>
                ) : (
                  <Image
                    className="pt-3 rounded-2xl m-auto select-none"
                    alt="Coffee"
                    src={el?.thumbnail}
                    width={300}
                    height={300}
                    priority
                  />
                )}
              </div>
            </Link>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
}
