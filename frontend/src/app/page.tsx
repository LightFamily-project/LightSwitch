'use client';
import { useState } from 'react';
import { FlagIcon } from 'lucide-react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

import { TableDataType, Variation } from '@/types/home';
import { ExampleData } from '@/constants/mockData';
import './styles.css';
import { HomeContext } from '@/contexts/HomeContext';
import MainContent from '@/components/home/mainContent';

const newFeatureFlagSchema = z.object({
  key: z.string().min(1, 'Key is required'),
  type: z.string(),
  enabled: z.boolean(),
  defaultValue: z.union([z.boolean(), z.number(), z.string()]),
  variations: z.array(
    z.object({
      key: z.string().min(1, 'Variation key is required'),
      value: z.union([z.boolean(), z.number(), z.string()]),
    }),
  ),
});

export type NewFeatureFlagType = z.infer<typeof newFeatureFlagSchema>;

export default function Home() {
  // Sheet 열고 닫기 핸들링
  const [isOpen, setIsOpen] = useState<boolean>(false);
  // 검색 필터링
  const [columnFilters, setColumnFilters] = useState<string>('');
  // 전체 데이터
  const [data, setData] = useState<TableDataType[]>(ExampleData);
  // variations 상태 관리
  const [variationsState, setVariationsState] = useState<Variation[]>([]);

  const form = useForm<NewFeatureFlagType>({
    resolver: zodResolver(newFeatureFlagSchema),
    defaultValues: {
      key: '',
      type: 'bool',
      enabled: false,
      defaultValue: false,
      variations: [],
    },
  });

  // 검색 기능
  function inputData(Data: string) {
    setColumnFilters(Data);
    const filteringData = data.filter((d) => d.Name.includes(Data));

    if (filteringData.length > 0) {
      setData(filteringData);
    }
  }

  // 데이터 초기화
  function initData() {
    setData(ExampleData);
  }

  // Variation 삭제
  const removeVariation = (index: number) => {
    const newVariations = form
      .getValues('variations')
      .filter((_d, i) => index !== i);

    // variations 상태 업데이트
    form.setValue('variations', newVariations);
    setVariationsState(newVariations);
  };

  // Variation 생성
  const addVariation = () => {
    // state 추가
    setVariationsState((prev) => [...prev, { key: '', value: '' }]);

    // form 로직
    const newType = form.getValues('type');
    const newVariation = {
      key: '',
      value: newType === 'boolean' ? false : newType === 'number' ? 0 : '',
    };

    const currentVariations = form.getValues('variations');
    form.setValue('variations', [...currentVariations, newVariation]);
  };

  function handleVariations(
    e: React.ChangeEvent<HTMLInputElement>,
    sort: 'key' | 'value',
    index: number,
  ) {
    const value = e.target.value;
    const newVariations = [...variationsState];

    newVariations[index][sort] = value;
    setVariationsState(newVariations);
    form.setValue(`variations.${index}.${sort}`, value);
  }

  // 데이터 추가
  function submit() {
    setData([
      ...data,
      {
        Name: form.getValues('key'),
        Type: form.getValues('type'),
        Created: '2024-01-15',
        By: 'Alice',
        Status: form.getValues('enabled'),
        'Default Value': form.getValues('defaultValue'),
      },
    ]);
  }

  // 사이드바 닫기
  const handleCreateFeatureFlag = () => {
    setIsOpen(false);
  };

  function fillData() {
    setData(ExampleData);
  }

  const handleSheetClose = () => {
    setIsOpen(!isOpen);
    form.reset();
  };

  return (
    <div className="flex h-full flex-col gap-2 p-6">
      <header className="flex w-[270px] flex-row items-center justify-between">
        <FlagIcon size={20} />
        <span className="text-[1.2rem] font-semibold">
          Feature Flag Management
        </span>
      </header>
      <div id="sideBar"></div>
      {isOpen && (
        <div
          className="absolute right-0 top-0 z-[49] size-full bg-[rgba(0,0,0,0.7)]"
          onClick={handleSheetClose}
        ></div>
      )}
      <HomeContext.Provider
        value={{
          columnFilters,
          initData,
          inputData,
          fillData,
          isOpen,
          setIsOpen,
          form,
          submit,
          removeVariation,
          addVariation,
          handleCreateFeatureFlag,
          data,
          setData,
          variationsState,
          setVariationsState,
          handleVariations,
          handleSheetClose,
        }}
      >
        <MainContent />
      </HomeContext.Provider>
    </div>
  );
}
