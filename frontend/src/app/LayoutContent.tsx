'use client';

import { FlagIcon, LogOutIcon, SettingsIcon } from 'lucide-react';
import Image from 'next/image';
import Link from 'next/link';
import { usePathname } from 'next/navigation';

import { ThemeToggle } from '@/components/theme-toggle';
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarProvider,
  SidebarRail,
  SidebarTrigger,
} from '@/components/ui/sidebar';
import { PATHS } from '@/constants/paths';
import { useAuth } from '@/contexts/AuthContext';

export function LayoutContent({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const { logout } = useAuth();

  const pathItems = [
    {
      title: 'Feature Flags',
      icon: FlagIcon,
      href: PATHS.FEATURE_FLAG_MANAGEMENT,
    },
    {
      title: 'SDK Management',
      icon: SettingsIcon,
      href: PATHS.SDK_MANAGEMENT,
    },
  ];

  if (pathname === PATHS.LOGIN || pathname === PATHS.SETUP) {
    return (
      <div className="relative flex min-h-screen flex-col bg-background">
        <header className="absolute right-8 top-8">
          <ThemeToggle />
        </header>
        <main className="flex grow items-center justify-center p-6">
          {children}
        </main>
      </div>
    );
  }

  return (
    <SidebarProvider>
      <div className="flex h-screen w-full bg-background text-foreground">
        <Sidebar collapsible="icon">
          <SidebarHeader className="flex h-12 w-full flex-row items-center gap-0">
            <Image
              className="h-12 w-auto"
              src="/images/LigthtSwitch.png"
              alt="LightSwitch Logo"
              width={48}
              height={48}
            />
            <h1 className="text-xl font-bold group-data-[collapsible=icon]:hidden">
              LightSwitch
            </h1>
            <SidebarTrigger className="ml-auto mr-0 group-data-[collapsible=icon]:hidden" />
          </SidebarHeader>

          <SidebarContent className="px-2">
            <SidebarMenu>
              {pathItems.map((pathItem) => (
                <SidebarMenuItem key={pathItem.title}>
                  <SidebarMenuButton
                    asChild
                    isActive={pathname === pathItem.href}
                  >
                    <Link href={pathItem.href}>
                      <pathItem.icon />
                      <span>{pathItem.title}</span>
                    </Link>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarContent>

          <SidebarFooter className="px-2 pb-4">
            <SidebarMenu>
              <SidebarMenuItem>
                <SidebarMenuButton asChild>
                  <ThemeToggle className="p-3" variant="ghost">
                    <span className="ml-2 transition-opacity group-data-[state=collapsed]:hidden">
                      Theme
                    </span>
                  </ThemeToggle>
                </SidebarMenuButton>
              </SidebarMenuItem>
              <SidebarMenuItem>
                <SidebarMenuButton onClick={logout}>
                  <LogOutIcon />
                  <span>Logout</span>
                </SidebarMenuButton>
              </SidebarMenuItem>
            </SidebarMenu>
          </SidebarFooter>
          <SidebarRail />
        </Sidebar>
        <main className="flex-1 overflow-auto">{children}</main>
      </div>
    </SidebarProvider>
  );
}
