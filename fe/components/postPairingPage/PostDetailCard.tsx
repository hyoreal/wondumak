import Tag from '../Tag';
import Image from 'next/image';
import { useEffect, useState } from 'react';
import {
  CoffeeCountryMatcherToKor,
  CoffeeCategoryMatcherToKor,
} from '@/utils/CoffeeMatcher';
import { TagMatcherToKor } from '@/utils/TagMatcher';

export default function PostDetailCard(props: any) {
  const [coffeeInfo, setCoffeeInfo] = useState(props);
  useEffect(() => {
    if (props.coffeeInfo !== undefined) setCoffeeInfo(props.coffeeInfo);
  }, [props]);

  return (
    <div className="flex rounded-lg bg-white text-y-black border border-y-lightGray px-3 py-5 my-2">
      {coffeeInfo?.coffeeDetailsBasic?.thumbnail.includes('.') ? (
        <div className="relative w-[130px] h-[160px]">
          <Image
            className="object-cover"
            alt={coffeeInfo?.coffeeDetailsBasic?.korName}
            src={coffeeInfo?.coffeeDetailsBasic?.thumbnail}
            fill
            sizes="50vw"
          />
        </div>
      ) : (
        <></>
      )}

      <div className="flex flex-col justify-center">
        <h1 className="font-bold text-xl sm:text-2xl lg:text-3xl">
          {coffeeInfo?.coffeeDetailsBasic?.korName}
        </h1>
        <div className="text-xs sm:text-sm lg:text-lg">
          <span>
            {coffeeInfo?.coffeeCategoryTypes === undefined ? (
              <></>
            ) : (
              coffeeInfo?.coffeeCategoryTypes?.map((el: string, idx: number) => {
                return (
                  <span className="mx-0.5" key={idx}>
                    {CoffeeCategoryMatcherToKor(el)}
                  </span>
                );
              })
            )}
          </span>
          <span>
            / {CoffeeCountryMatcherToKor(coffeeInfo?.coffeeDetailsBasic?.country)}
          </span>
          <span> / {coffeeInfo?.coffeeDetailsBasic?.roasting}%</span>
          {coffeeInfo?.coffeeDetailsBasic?.acidity === null ? (
            <></>
          ) : (
            <span>/ {coffeeInfo?.coffeeDetailsBasic?.acidity} Acidity</span>
          )}
        </div>
        <div className="my-2">
          <span className="font-semibold sm:text-xl lg:text-2xl">
            ⭐️ {coffeeInfo?.coffeeDetailsStars?.totalAverageStars}
          </span>
          <span className="text-y-gray ml-1 text-xs sm:text-sm lg:text-lg">
            ({coffeeInfo?.coffeeDetailsCounts?.ratingCount} ratings)
          </span>
        </div>
        <div>
          {coffeeInfo?.coffeeDetailsTopTags === null ? (
            <></>
          ) : (
            <div className="flex flex-wrap -mt-1 h-fit">
              {coffeeInfo?.coffeeDetailsTopTags?.map((el: string, idx: number) => {
                return <Tag key={idx}>{TagMatcherToKor(el)}</Tag>;
              })}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
