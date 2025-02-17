import { FlagIcon } from 'lucide-react';

import { columns } from '@/components/ui/featureFlagColumns';
import { DataTable } from '@/components/ui/TanstackTable';
import { ExampleData } from '@/constants/tableMockDatat';

export default function Home() {
  return (
    <div className="flex size-full flex-col gap-5 p-3">
      <header className="flex items-center gap-3">
        <FlagIcon size={20} />
        <span className="text-xl font-semibold">Feature Flag Management</span>
      </header>

      <DataTable columns={columns} data={ExampleData} />
    </div>
  );
}
