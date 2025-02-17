import { columns } from '@/components/ui/columns';
import { DataTable } from '@/components/ui/dataTable';
import { ExampleData } from '@/constants/tableMockDatat';
import { TableDataType } from '@/types/home';
import { FlagIcon } from 'lucide-react';

export default function Home() {
  return (
    <div className="flex w-full flex-col gap-5 p-3">
      <header className="flex w-[270px] flex-row items-center justify-between">
        <FlagIcon size={20} />
        <span className="text-[1.2rem] font-semibold">
          Feature Flag Management
        </span>
      </header>

      <DataTable columns={columns} data={ExampleData as TableDataType[]} />
    </div>
  );
}
