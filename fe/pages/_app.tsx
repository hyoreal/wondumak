import Header from '@/components/Header';
import '@/styles/globals.css';
import type { AppProps } from 'next/app';
import { RecoilRoot } from 'recoil';
import NavBar from '@/components/NavBar';
import { useState } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

declare global {
  interface Window {
    Kakao: any;
  }
}

export default function App({ Component, pageProps }: AppProps) {
  const [queryClient] = useState(() => new QueryClient());

  return (
    <RecoilRoot>
      <QueryClientProvider client={queryClient}>
        <Header />
        <Component {...pageProps} />
        <NavBar />
      </QueryClientProvider>
    </RecoilRoot>
  );
}
