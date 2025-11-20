import React, { useState, useEffect, useRef } from 'react';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

interface Message {
  senderId: number;
  senderNickname: string;
  content: string;
  createdAt: string;
}

const ChatRoom = ({ roomId, userId }: { roomId: number; userId: number }) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [content, setContent] = useState('');
  const stompClient = useRef<any>(null);

  useEffect(() => {
    const socket = new SockJS(`${process.env.NEXT_PUBLIC_API_URL}/ws`);
    stompClient.current = Stomp.over(socket);

    stompClient.current.connect({}, () => {
      stompClient.current.subscribe(`/topic/chat/${roomId}`, (message: any) => {
        const newMessage = JSON.parse(message.body);
        setMessages((prev) => [...prev, newMessage]);
      });
    });

    // Fetch existing messages
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/chat/${roomId}/messages`)
      .then(res => res.json())
      .then(data => setMessages(data));

    return () => {
      if (stompClient.current) {
        stompClient.current.disconnect();
      }
    };
  }, [roomId]);

  const sendMessage = () => {
    if (content.trim() && stompClient.current) {
      const message = {
        roomId,
        senderId: userId,
        content
      };
      stompClient.current.send(`/app/chat/${roomId}`, {}, JSON.stringify(message));
      setContent('');
    }
  };

  return (
    <div className="flex flex-col h-[500px] w-[400px] border rounded shadow-lg bg-white">
      <div className="flex-1 overflow-y-auto p-4 space-y-2">
        {messages.map((msg, idx) => (
          <div key={idx} className={`flex flex-col ${msg.senderId === userId ? 'items-end' : 'items-start'}`}>
            <span className="text-xs text-gray-500">{msg.senderNickname}</span>
            <div className={`p-2 rounded-lg ${msg.senderId === userId ? 'bg-y-brown text-white' : 'bg-gray-200'}`}>
              {msg.content}
            </div>
          </div>
        ))}
      </div>
      <div className="p-4 border-t flex">
        <input
          type="text"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          className="flex-1 border rounded p-2 mr-2"
          placeholder="Type a message..."
          onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
        />
        <button onClick={sendMessage} className="bg-y-brown text-white px-4 py-2 rounded">
          Send
        </button>
      </div>
    </div>
  );
};

export default ChatRoom;
