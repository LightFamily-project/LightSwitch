import { X } from 'lucide-react';
import { useContext } from 'react';
import { FormProvider } from 'react-hook-form';

import CreateBtnSection from './createBtnSection';
import DefaultValueInputSection from './defaultValueInputSection';
import EnabledInputSection from './enabledInputSection';
import KeyInputSection from './keyInputSection';
import SelectSection from './selectSection';
import VariationsSection from './variationsSection';

import { HomeContext, homeContextType } from '@/contexts/HomeContext';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';

export default function SideBarContent() {
  const context = useContext<homeContextType | null>(HomeContext);

  const { handleSheetClose, submit, form } = context as homeContextType;

  return (
    <div className="flex min-h-full flex-col bg-white p-5">
      <header className="mb-2 flex w-full flex-row items-center justify-between">
        <span className="text-[1.3rem]">Add New Feature Flag</span>
        <X
          className="size-[25px] cursor-pointer rounded-[50%] border-2 border-white p-1 hover:border-2 hover:border-[#3b84e3]"
          onClick={handleSheetClose}
        />
      </header>
      <main>
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
      </main>
    </div>
  );
}
