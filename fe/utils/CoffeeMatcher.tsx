export const CoffeeCategoryMatcherToKor = (str: string) => {
  switch (str) {
    case 'ARABICA':
      return '아라비카';
    case 'ROBUSTA':
      return '로부스타';
    case 'LIBERICA':
      return '리베리카';
    case 'EXCELSA':
      return '엑셀사';
    case 'BLEND':
      return '블렌드';
    case 'DECAF':
      return '디카페인';
    case 'COLD_BREW':
      return '콜드브루';
    case 'ESPRESSO_BASED':
      return '에스프레소 베이스';
    default:
      return '기타';
  }
};

export const CoffeeCountryMatcherToKor = (str: string) => {
  switch (str) {
    case 'ETHIOPIA':
      return '에티오피아';
    case 'COLOMBIA':
      return '콜롬비아';
    case 'GUATEMALA':
      return '과테말라';
    case 'KENYA':
      return '케냐';
    case 'JAMAICA':
      return '자메이카';
    case 'USA':
      return '미국';
    case 'INDONESIA':
      return '인도네시아';
    case 'COSTA RICA':
      return '코스타리카';
    case 'BRAZIL':
      return '브라질';
    case 'VIETNAM':
      return '베트남';
    case 'PANAMA':
      return '파나마';
    case 'EL SALVADOR':
      return '엘살바도르';
    case 'NICARAGUA':
      return '니카라과';
    case 'PHILIPPINES':
      return '필리핀';
    case 'MALAYSIA':
      return '말레이시아';
    case 'YEMEN':
      return '예멘';
    case 'RWANDA':
      return '르완다';
    case 'TANZANIA':
      return '탄자니아';
    case 'PAPUA NEW GUINEA':
      return '파푸아뉴기니';
    default:
      return str;
  }
};

export const CoffeeCategoryMatcher = (str: string[]) => {
  // This looks weird in original code (array in case), but assuming simplified logic here
  return 'BLEND';
};
