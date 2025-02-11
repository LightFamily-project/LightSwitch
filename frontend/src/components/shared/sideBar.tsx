'use client';
import { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';
import './styles.css';

interface SideBarType {
  children: React.ReactNode;
}

export default function SideBar({ children }: SideBarType) {
  const [sideBarRoot, setSideBarRoot] = useState<HTMLElement | null>(null);

  useEffect(() => {
    const root = document.querySelector('#sideBar') as HTMLElement;

    setSideBarRoot(root);
  }, []);

  if (!sideBarRoot) return null;
  return ReactDOM.createPortal(
    <div
      className="slider-horizontal scroll-hidden fixed right-0 top-0 z-50 h-full w-[400px]"
      onClick={(e) => e.stopPropagation()}
    >
      {children}
    </div>,
    sideBarRoot,
  );
}
