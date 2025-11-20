export interface CoffeeInfo {
  coffeeId: number;
  coffeeDetailsBasic: {
    korName: string;
    engName: string;
    country: string;
    thumbnail: string;
    roasting: number;
    acidity: number;
  };
  coffeeCategoryTypes: string[];
  coffeeDetailsTopTags: string[] | null;
  coffeeDetailsStars: {
    totalAverageStars: number;
    femaleAverageStars: number;
    maleAverageStars: number;
  };
  coffeeDetailsCounts: {
    totalStarCount: number;
    femaleStarCount: number;
    maleStarCount: number;
    ratingCount: number;
    pairingCount: number;
  };
  isWishListed: boolean;
  similarCoffees: CoffeeInfo[];
}

export interface RatingInfo {
  data: RatingCardProps[];
  pageInfo: {
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    coffeeId: number;
    coffeeKorName: string;
    coffeeEngName: string;
  };
}

export interface RatingCardProps {
  coffeeId: number;
  ratingId: number;
  korName: string;
  userId: number;
  nickname: string;
  userImage: string;
  star: number;
  ratingTag: [string, string, string, string];
  content: string;
  likeCount: number;
  commentCount: number;
  createdAt: string;
  modifiedAt: string;
  isUserLikes: boolean;
}

export interface PairingInfo {
  data: PairingCardProps[];
  pageInfo: {
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    coffeeId: number;
    coffeeKorName: string;
    coffeeEngName: string;
  };
}

export interface PairingCardProps {
  coffeeId: number;
  korName: string;
  pairingId: number;
  nickname: string;
  userImage: string;
  content: string;
  thumbnail: string;
  category: string;
  likeCount: number;
  commentCount: number;
  isUserLikes: boolean;
  createdAt: string;
  modifiedAt: string;
}

export interface SimilarCoffeeProps {
  coffeeId: number;
  korName: string;
  country: string;
  coffeeCategories: [
    {
      coffeeCategoryType: string;
    }
  ];
  averageStar: number;
  starCount: number;
  thumbnail: string;
  roasting: number;
  acidity: number | null;
}

export interface PopularCoffeeType {
  coffeeId: number;
  korName: string;
  thumbnail: string;
  averageStar: number;
  coffeeCategories: any;
  country: string;
  acidity: number | null;
  roasting: number;
}

export interface RecommendCoffeeType {
  averageStar: number;
  coffeeCategories: any;
  coffeeId: number;
  korName: string;
  thumbnail: string;
  country: string;
  acidity: number | null;
  roasting: number;
}
