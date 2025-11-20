import React from 'react';
import { useRouter } from 'next/router';
import ChatRoom from '@/components/chat/ChatRoom';

const ChatPage = () => {
  const router = useRouter();
  const { id } = router.query;
  // Assume user ID is available from context or local storage
  const userId = 1; // Mock ID

  if (!id) return <div>Loading...</div>;

  return (
    <div className="flex justify-center items-center h-screen bg-y-cream">
      <ChatRoom roomId={Number(id)} userId={userId} />
    </div>
  );
};

export default ChatPage;
