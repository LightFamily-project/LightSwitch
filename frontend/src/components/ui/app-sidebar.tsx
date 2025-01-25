import {
  Boxes,
  CirclePlay,
  LayoutDashboard,
  Search,
  SquareChartGantt,
  Users,
} from 'lucide-react';
import Image from 'next/image';

import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar';

export function AppSidebar() {
  const LightSwitchPath = '/images/LigthtSwitch.png';

  const tabs = [
    {
      icon: LayoutDashboard,
      tabName: 'Dashboard',
    },
    {
      icon: SquareChartGantt,
      tabName: 'Projects',
    },
    {
      icon: Search,
      tabName: 'Search',
    },
    {
      icon: CirclePlay,
      tabName: 'Playground',
    },
    {
      icon: SquareChartGantt,
      tabName: 'Insights',
    },
  ];
  const configures = [
    {
      icon: Boxes,
      tabName: 'SDK Management',
    },
    {
      icon: Users,
      tabName: 'Segments',
    },
  ];

  return (
    <Sidebar>
      <SidebarHeader style={{ backgroundColor: '#18181B' }}>
        <div className="flex items-center gap-2 p-3">
          <Image
            className="rounded-md bg-white"
            src={LightSwitchPath}
            width={40}
            height={40}
            alt="로고"
          />
          <span className="font-medium text-white">LightSwitch</span>
        </div>
      </SidebarHeader>
      <SidebarContent style={{ backgroundColor: '#18181B' }}>
        <SidebarGroup>
          <SidebarGroupContent>
            <SidebarMenu>
              {tabs.map((item, i) => (
                <SidebarMenuItem key={i}>
                  <SidebarMenuButton asChild>
                    <a href={'#'}>
                      <item.icon />
                      <span>{item.tabName}</span>
                    </a>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
        <SidebarGroupLabel>configure</SidebarGroupLabel>
        <SidebarGroup>
          <SidebarGroupContent>
            <SidebarMenu>
              {configures.map((item, i) => (
                <SidebarMenuItem key={i}>
                  <SidebarMenuButton asChild>
                    <a href={'#'}>
                      <item.icon />
                      <span>{item.tabName}</span>
                    </a>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  );
}
