/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    domains: [
      'phinf.pstatic.net',
      'lh3.googleusercontent.com',
      'k.kakaocdn.net',
      'worldcoffeemarket.kr',
      't1.daumcdn.net',
      'assacoffee.com',
      'getacoffee.s3.ap-northeast-2.amazonaws.com',
      's3.ap-northeast-2.amazonaws.com',
    ],
  },
  reactStrictMode: true,
  env: {
    API_URL: process.env.API_URL,
  },
};

module.exports = nextConfig;
