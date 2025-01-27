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

export function LayoutContent({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();

  const pathItems = [
    {
      title: 'Feature Flags',
      icon: FlagIcon,
      href: '/',
    },
    {
      title: 'SDK Management',
      icon: SettingsIcon,
      href: '/sdk',
    },
  ];

  return (
    <SidebarProvider>
      <div className="flex h-screen bg-background text-foreground">
        <Sidebar collapsible="icon">
          <SidebarHeader className="flex h-12 w-full flex-row items-center gap-0">
            <Image
              className="h-12 w-auto"
              src="/images/LigthtSwitch.png"
              alt="LightSwitch Logo"
              width={48}
              height={48}
            />
            <h1 className="text-xl font-bold transition-opacity group-data-[state=collapsed]:hidden">
              LightSwitch
            </h1>
            <SidebarTrigger className="ml-auto mr-0 transition-opacity group-data-[state=collapsed]:hidden" />
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
                  <ThemeToggle className="p-3">
                    <span className="ml-2 transition-opacity group-data-[state=collapsed]:hidden">
                      Theme
                    </span>
                  </ThemeToggle>
                </SidebarMenuButton>
              </SidebarMenuItem>
              <SidebarMenuItem>
                <SidebarMenuButton>
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
