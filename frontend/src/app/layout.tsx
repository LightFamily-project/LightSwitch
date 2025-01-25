// css
import './globals.css';
import '@/app/styles.css';
import type { Metadata } from 'next';
// font
import { Geist, Geist_Mono } from 'next/font/google';

// components
import { AppSidebar } from '@/components/ui/app-sidebar';
import { SidebarProvider, SidebarTrigger } from '@/components/ui/sidebar';

const geistSans = Geist({
  variable: '--font-geist-sans',
  subsets: ['latin'],
});

const geistMono = Geist_Mono({
  variable: '--font-geist-mono',
  subsets: ['latin'],
});

export const metadata: Metadata = {
  title: 'Light Switch',
  description: 'A lightweight, open-source, self-hosted feature toggle tool.',
};

export default function RootLayout({ children }: Readonly) {
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        <SidebarProvider>
          <AppSidebar />
          <main className="flex-1 bg-[#09090B]">
            <SidebarTrigger
              className="sidebarController"
              style={{ color: 'white' }}
            />
            <div className="flex min-h-screen items-center justify-center">
              <div className="w-full">{children}</div>
            </div>
          </main>
        </SidebarProvider>
      </body>
    </html>
  );
}
