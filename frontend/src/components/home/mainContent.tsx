import { Lock, Plus, RefreshCcw } from 'lucide-react';
import { FormProvider } from 'react-hook-form';
import { useContext, useEffect } from 'react';
import { useTheme } from 'next-themes';

import { Card, CardContent, CardHeader, CardTitle } from '../ui/card';
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from '../ui/sheet';
import { Input } from '../ui/input';
import { DataTable } from '../ui/dataTable';

import KeyInputSection from './mainContent/keyInputSection';
import EnabledInputSection from './mainContent/enabledInputSection';
import DefaultValueInputSection from './mainContent/defaultValueInputSection';
import VariationsSection from './mainContent/variationsSection';
import CreateBtnSection from './mainContent/createBtnSection';
import SelectSection from './mainContent/selectSection';

import { columns } from '@/constants/columns';
import { TableDataType } from '@/types/home';
import { HomeContext, homeContextType } from '@/contexts/HomeContext';

export default function MainContent() {
  const context = useContext<homeContextType | null>(HomeContext);

  const {
    columnFilters,
    initData,
    inputData,
    fillData,
    isOpen,
    setIsOpen,
    form,
    submit,
    data,
    setData,
  } = context as homeContextType;

  const { theme } = useTheme();

  useEffect(() => {
    console.log('theme: ', theme);
  }, [theme]);

  const handleSheetClose = () => {
    setIsOpen(!isOpen); // 시트 닫기
    form.reset(); // 폼 초기화
  };
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
          <Sheet open={isOpen} onOpenChange={handleSheetClose}>
            {theme === 'dark' ? (
              <SheetTrigger className="flex h-[36px] w-[230px] flex-row items-center justify-between rounded-[0.6rem] border border-input bg-transparent p-2 text-[0.9rem] text-white hover:border-2 hover:bg-input hover:text-white">
                <Plus width={18} />
                New feature flag
                <Lock width={18} />
              </SheetTrigger>
            ) : (
              <SheetTrigger className="flex h-[36px] w-[230px] flex-row items-center justify-between rounded-[0.6rem] border border-input p-2 text-[0.9rem] text-black hover:border-2 hover:border-input hover:bg-input hover:text-black">
                <Plus width={18} />
                New feature flag
                <Lock width={18} />
              </SheetTrigger>
            )}

            <SheetContent className="scroll-hidden">
              <SheetHeader className="mb-2">
                <SheetTitle className="text-[1.3rem]">
                  Add New Feature Flag
                </SheetTitle>
              </SheetHeader>

              <Card>
                <CardHeader>
                  <CardTitle>New Feature Flag Details</CardTitle>
                </CardHeader>
                <CardContent className="overflow-y-auto">
                  <FormProvider {...form}>
                    <form
                      onSubmit={(e) => {
                        e.preventDefault();
                        submit();
                      }}
                    >
                      <div className="mb-4 flex flex-col gap-3">
                        <KeyInputSection />
                        <SelectSection />
                        <EnabledInputSection />
                        <DefaultValueInputSection />
                      </div>
                      <VariationsSection />
                      <CreateBtnSection />
                    </form>
                  </FormProvider>
                </CardContent>
              </Card>
            </SheetContent>
          </Sheet>
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
