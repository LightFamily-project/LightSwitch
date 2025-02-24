import { ColumnDef } from '@tanstack/react-table';

import { FeatureFlagTableType } from '@/types/types';

export const columns: ColumnDef<FeatureFlagTableType>[] = [
  {
    accessorKey: 'Key',
    header: 'Key',
  },
  {
    accessorKey: 'type',
    header: 'type',
  },
  {
    accessorKey: 'createdAt',
    header: 'createdAt',
  },
  {
    accessorKey: 'createdBy',
    header: 'createdBy',
  },
  {
    accessorKey: 'status',
    header: 'status ',
  },
];
