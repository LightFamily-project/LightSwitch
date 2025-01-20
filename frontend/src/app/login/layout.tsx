import type { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Login | Light Switch',
  description: 'Login to your Light Switch account',
};

export default function LoginLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-100">
      <div className="w-full max-w-md">{children}</div>
    </div>
  );
}
