/** @type {import('tailwindcss').Config} */

module.exports = {
  content: [
    './app/**/*.{js,ts,jsx,tsx}',
    './pages/**/*.{js,ts,jsx,tsx}',
    './components/**/*.{js,ts,jsx,tsx}',

    // Or if using `src` directory:
    './src/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Gmarket Sans', 'Arial', 'sans-serif'],
        // sans가 제일 기본 상속 폰트이므로 전체 폰트바꾸려면 sans재지정후 맨앞에 원하는 폰트 넣기
      },
      colors: {
        'y-brown': '#87CEEB', // Sky Blue replaced Brown
        'y-cream': '#E0F7FA', // Light Cyan replaced Cream
        'y-gold': '#00BFFF', // Deep Sky Blue replaced Gold
        'y-lightGray': '#DDDDDD',
        'y-yellow': '#B0E0E6', // Powder Blue replaced Yellow
        'y-lemon': '#F0F8FF', // Alice Blue replaced Lemon
        'y-gray': '#A7A7A7',
        'y-black': '#000000',
      },
    },
  },
  plugins: [],
};
