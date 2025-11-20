import React, { useState, useEffect } from 'react';
import Link from 'next/link';

interface Room {
  roomId: number;
  title: string;
}

const ChatList = () => {
  const [rooms, setRooms] = useState<Room[]>([]);

  useEffect(() => {
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/chat/rooms`, {
        headers: {
            // Add auth token if needed
        }
    })
      .then(res => res.json())
      .then(data => setRooms(data));
  }, []);

  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold mb-4">My Chat Rooms</h2>
      <div className="space-y-2">
        {rooms.map((room) => (
          <Link key={room.roomId} href={`/chat/${room.roomId}`}>
            <div className="p-4 border rounded hover:bg-gray-50 cursor-pointer">
              {room.title}
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
};

export default ChatList;
