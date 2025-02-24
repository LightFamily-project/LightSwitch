import { FlagIcon } from 'lucide-react';

import { columns } from '@/constants/featureFlagColumns';
import { TanstackTable } from '@/components/TanstackTable';
import { ExampleData } from '@/constants/tableMockDatat';

export default function Home() {
  return (
    <div className="flex size-full flex-col gap-5 p-3">
      <header className="flex items-center gap-3">
        <FlagIcon size={20} />
        <span className="text-xl font-semibold">Feature Flag Management</span>
      </header>

      <TanstackTable columns={columns} data={ExampleData} />
    </div>
  );
}
