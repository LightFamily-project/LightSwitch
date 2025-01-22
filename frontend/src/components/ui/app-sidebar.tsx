import { Boxes, CirclePlay, LayoutDashboard, Search, SquareChartGantt, Users } from "lucide-react"

import Image from "next/image";

export function AppSidebar() {
  const LightSwitchPath = "/images/LigthtSwitch-2.png";
  
  return (
    <div className="flex h-screen bg-background">
      <div className="w-64 border-r bg-[#18181B] p-4">
        <div className="flex items-center gap-2 mb-6">
          <div className="h-8 w-8 rounded-md bg-primary" />
          <Image className="bg-white rounded-md" src={LightSwitchPath} width={40} height={40} alt="로고"/>
          <span className="font-medium text-white">LightSwitch</span>
        </div>

        <nav className="space-y-2">
          <div className="flex flex-row px-3 py-2 rounded-md hover:bg-dashboard-tab-selected">
            <LayoutDashboard size={20} className="mr-3" color="white"/>
            <a href="#" className="flex items-center gap-2 text-sm font-normal rounded-md text-dashboard-text-color">
              Dashboard
            </a>
          </div>

          <div className="flex flex-row px-3 py-2 rounded-md hover:bg-dashboard-tab-selected">
            <SquareChartGantt size={20} className="mr-3" color="white"/>
            <a href="#" className="flex items-center gap-2 text-sm font-normal rounded-md text-dashboard-text-color">
              Projects
            </a>
          </div>

          <div className="flex flex-row px-3 py-2 rounded-md hover:bg-dashboard-tab-selected">
            <Search size={20} className="mr-3" color="white"/>
            <a href="#" className="flex items-center gap-2 text-sm font-normal rounded-md text-dashboard-text-color">
              Search
            </a>
          </div>

          <div className="flex flex-row px-3 py-2 rounded-md hover:bg-dashboard-tab-selected">
            <CirclePlay size={20} className="mr-3" color="white"/>
            <a href="#" className="flex items-center gap-2 text-sm font-normal rounded-md text-dashboard-text-color">
              Playground
            </a>
          </div>

          <div className="flex flex-row px-3 py-2 rounded-md hover:bg-dashboard-tab-selected">
            <SquareChartGantt size={20} className="mr-3" color="white"/>
            <a href="#" className="flex items-center gap-2 text-sm font-normal rounded-md text-dashboard-text-color">
              Insights
            </a>
          </div>
        </nav>

        <div className="mt-8">
          <h3 className="px-3 text-xs font-medium mb-2 text-dashboard-text-color">Configure</h3>
          <nav className="space-y-2">
            <div className="flex flex-row px-3 py-2 rounded-md hover:bg-dashboard-tab-selected">
              <Boxes size={20} className="mr-3" color="white"/>
              <a href="#" className="flex items-center gap-2 text-sm font-normal rounded-md text-dashboard-text-color">
                SDK Management
              </a>
            </div>

            <div className="flex flex-row px-3 py-2 rounded-md hover:bg-dashboard-tab-selected">
              <Users size={20} className="mr-3" color="white"/>
              <a href="#" className="flex items-center gap-2 text-sm font-normal rounded-md text-dashboard-text-color">
                Segments
              </a>
            </div>
          </nav>
        </div>
      </div>
    </div>
  )
}