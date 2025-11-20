import { UseFormRegister, RegisterOptions } from 'react-hook-form';
interface IFormValues {
  userCoffeeTags: Array<string>;
  gender: string;
  age: string;
  userCoffeeCategories: Array<string>;
  nickname: string;
  image: string[];
}
type InputProps = {
  register: UseFormRegister<IFormValues>;
  rules?: RegisterOptions;
};
export default function CoffeeCategory({ rules, register }: InputProps) {
  const coffeeCategory = [
    {
      type: 'ARABICA',
      text: '아라비카',
    },
    {
      type: 'ROBUSTA',
      text: '로부스타',
    },
    {
      type: 'LIBERICA',
      text: '리베리카',
    },
    {
      type: 'EXCELSA',
      text: '엑셀사',
    },
    {
      type: 'BLEND',
      text: '블렌드',
    },
    {
      type: 'DECAF',
      text: '디카페인',
    },
    {
      type: 'COLD_BREW',
      text: '콜드브루',
    },
    {
      type: 'ESPRESSO_BASED',
      text: '에스프레소',
    },
  ];

  return (
    <div className="px-2 pt-2">
      <div className="text-sm">선호 커피 (최대 2개까지 선택 가능)</div>
      <div className="grid grid-cols-4 my-2 gap-1 w-full items-center">
        {coffeeCategory.map((el: any, idx: number) => (
          <div key={idx.toString()}>
            <input
              type="checkbox"
              id={el.type}
              value={el.type}
              className="peer hidden"
              {...(register && register('userCoffeeCategories', rules))}
            />
            <label
              htmlFor={el.type}
              className="text-xs h-10 w-full inline-flex items-center justify-center cursor-pointer select-none rounded-xl p-1 text-center peer-checked: border-2 peer-checked:border-y-brown"
            >
              {el.text}
            </label>
          </div>
        ))}
      </div>
    </div>
  );
}
