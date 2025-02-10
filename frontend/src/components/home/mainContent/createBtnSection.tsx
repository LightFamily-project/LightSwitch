import { useContext } from 'react';

import { Button } from '@/components/ui/button';
import { HomeContext, homeContextType } from '@/contexts/HomeContext';

export default function CreateBtnSection() {
  const context = useContext<homeContextType | null>(HomeContext);

  const { handleCreateFeatureFlag } = context as homeContextType;

  return (
    <div className="flex w-full flex-row justify-end">
      <Button
        className="flex flex-row items-center justify-between rounded-[0.3rem] text-[0.8rem]"
        type="submit"
        variant="outline"
        onClick={handleCreateFeatureFlag}
      >
        Create Feature Flag
      </Button>
    </div>
  );
}
