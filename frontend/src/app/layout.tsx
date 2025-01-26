import type { Metadata } from 'next';
import { Inter } from 'next/font/google';

import { LayoutContent } from './LayoutContent';

import { ThemeProvider } from '@/components/theme-provider';

import './globals.css';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'Light Switch',
  description: 'A lightweight, open-source, self-hosted feature toggle tool.',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className={inter.className}>
        <ThemeProvider attribute="class" enableSystem disableTransitionOnChange>
          <LayoutContent>{children}</LayoutContent>
        </ThemeProvider>
      </body>
    </html>
  );
}
