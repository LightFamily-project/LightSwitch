import { SetStateAction, createContext } from 'react';
import { UseFormReturn } from 'react-hook-form';

import { NewFeatureFlagType } from '@/app/page';
import { TableDataType, Variation } from '@/types/home';

export interface homeContextType {
  columnFilters: string;
  initData(): void;
  inputData(Data: string): void;
  fillData(): void;
  isOpen: boolean;
  setIsOpen: React.Dispatch<SetStateAction<boolean>>;
  form: UseFormReturn<NewFeatureFlagType>;
  submit(): void;
  removeVariation: (index: number) => void;
  addVariation: () => void;
  handleCreateFeatureFlag: () => void;
  setData: React.Dispatch<SetStateAction<TableDataType[]>>;
  data: TableDataType[];
  variationsState: Variation[];
  setVariationsState: React.Dispatch<SetStateAction<Variation[]>>;
  handleVariations(
    e: React.ChangeEvent<HTMLInputElement>,
    sort: string,
    index: number,
  ): void;
  handleSheetClose: () => void;
}

export const HomeContext = createContext<homeContextType | null>(null);
