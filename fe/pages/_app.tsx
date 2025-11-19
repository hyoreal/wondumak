import Header from '@/components/Header';
import '@/styles/globals.css';
import type { AppProps } from 'next/app';
import { RecoilRoot } from 'recoil';
import NavBar from '@/components/NavBar';
import { useState } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import ChatbotIcon from '@/components/chatbot/ChatbotIcon';
import ChatbotModal from '@/components/chatbot/ChatbotModal';

declare global {
  interface Window {
    Kakao: any;
  }
}

export default function App({ Component, pageProps }: AppProps) {
  const [queryClient] = useState(() => new QueryClient());
  const [isChatbotOpen, setIsChatbotOpen] = useState(false);

  const toggleChatbot = () => {
    setIsChatbotOpen((prev) => !prev);
  };

  return (
    <RecoilRoot>
      <QueryClientProvider client={queryClient}>
        <Header />
        <Component {...pageProps} />
        <NavBar />
        <ChatbotIcon onClick={toggleChatbot} />
        {isChatbotOpen && <ChatbotModal onClose={toggleChatbot} />}
      </QueryClientProvider>
    </RecoilRoot>
  );
}
