import { Lock, Plus, RefreshCcw } from 'lucide-react';
import { useContext } from 'react';
import { useTheme } from 'next-themes';

import { Button } from '../ui/button';
import SideBar from '../shared/sideBar';
import { DataTable } from '../ui/dataTable';
import { Input } from '../ui/input';

import SideBarContent from './mainContent/sideBarContent';

import { HomeContext, homeContextType } from '@/contexts/HomeContext';
import { TableDataType } from '@/types/home';
import { columns } from '@/constants/columns';

export default function MainContent() {
  const context = useContext<homeContextType | null>(HomeContext);

  const {
    columnFilters,
    initData,
    inputData,
    fillData,
    isOpen,
    data,
    setData,
    handleSheetClose,
  } = context as homeContextType;

  const { theme } = useTheme();

  return (
    <main className="flex flex-col justify-between gap-2">
      <div className="flex w-full flex-row justify-end">
        <div className="flex w-[550px] flex-row items-center justify-between">
          <RefreshCcw
            className="cursor-pointer"
            onClick={() => initData()}
            size={18}
          />
          <Input
            className="flex w-[270px] items-center py-4"
            placeholder="Search and Filter (⌘+Shift+K)"
            value={columnFilters}
            onChange={(e) => inputData(e.target.value)}
            onBlur={fillData}
          />

          {theme === 'dark' ? (
            <Button
              className="flex h-[36px] w-[230px] flex-row items-center justify-between rounded-[0.6rem] border border-input bg-transparent p-2 text-[0.9rem] text-white hover:border-2 hover:bg-input hover:text-white"
              onClick={handleSheetClose}
            >
              <Plus width={18} />
              New feature flag
              <Lock width={18} />
            </Button>
          ) : (
            <Button
              className="flex h-[36px] w-[230px] flex-row items-center justify-between rounded-[0.6rem] border border-input bg-white p-2 text-[0.9rem] text-black hover:border-2 hover:border-input hover:bg-input hover:text-black"
              onClick={handleSheetClose}
            >
              <Plus width={18} />
              New feature flag
              <Lock width={18} />
            </Button>
          )}

          {isOpen && (
            <SideBar>
              <SideBarContent />
            </SideBar>
          )}
        </div>
      </div>

      <DataTable
        columns={columns}
        data={data as TableDataType[]}
        setData={setData}
      />
    </main>
  );
}
