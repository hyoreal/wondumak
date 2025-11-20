import Tag from '../Tag';
import Image from 'next/image';
import {
  CoffeeCategoryMatcherToKor,
  CoffeeCountryMatcherToKor,
} from '@/utils/CoffeeMatcher';
import { TagMatcherToKor } from '@/utils/TagMatcher';

export interface SearchCardProps {
  coffeeId: number;
  korName: string;
  country: string;
  category: string[];
  roasting: number | null;
  acidity: number | null;
  coffeeDetailsTopTags: [string, string, string, string] | null;
  totalAverageStars: number;
  totalStarCount: number;
  thumbnail: string;
}

export default function SearchCard(props: {
  cardProps: SearchCardProps;
  idx: number;
}) {
  return (
    <div className="flex rounded-lg text-y-black border border-y-lightGray m-2 min-h-[160px]">
      <div className="flex-none">
        <div className="relative w-[130px] h-[160px]">
          <Image
            alt={props.cardProps?.korName}
            src={props.cardProps?.thumbnail}
            fill
            sizes="50vw"
            className="object-cover rounded-lg"
          />
        </div>
      </div>
      <div
        className={`${
          props.idx % 2 === 0 ? 'bg-y-cream' : 'bg-y-lemon'
        } flex-auto flex justify-center py-4 rounded-r-lg`}
      >
        <div className="flex flex-col justify-center items-center">
          <h1 className="mb-2 font-bold text-lg mx-3">
            {props.cardProps?.korName}
          </h1>
          <div className="text-xs sm:text-sm">
            <span>
              {props.cardProps?.category.map((el: string, idx: number) => {
                return (
                  <span className="mx-0.5" key={idx}>
                    {CoffeeCategoryMatcherToKor(el)}
                  </span>
                );
              })}
            </span>
            <span>/ {CoffeeCountryMatcherToKor(props.cardProps?.country)}</span>
            <span>/ {props.cardProps?.roasting}%</span>
            {props.cardProps?.acidity ? (
              <span>/ {props.cardProps?.acidity} Acidity</span>
            ) : null}
          </div>
          <div className="my-4">
            <span className="font-semibold sm:text-xl lg:text-2xl">
              ⭐️ {props.cardProps?.totalAverageStars}
            </span>
            <span className="text-y-gray ml-1 text-xs sm:text-sm lg:text-lg">
              ({props.cardProps?.totalStarCount} ratings)
            </span>
          </div>
          <div className="flex flex-wrap mx-3">
            {props.cardProps?.coffeeDetailsTopTags
              ? props.cardProps?.coffeeDetailsTopTags.map(
                  (el: string, idx: number) => {
                    return <Tag key={idx}>{TagMatcherToKor(el)}</Tag>;
                  }
                )
              : null}
          </div>
        </div>
      </div>
    </div>
  );
}
