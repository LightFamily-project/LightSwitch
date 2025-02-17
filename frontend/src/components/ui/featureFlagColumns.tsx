'use client';

import { ColumnDef } from '@tanstack/react-table';

import { featureFlagTableType } from '@/types/types';

export const columns: ColumnDef<featureFlagTableType>[] = [
  {
    accessorKey: 'Key',
    header: 'Key',
  },
  {
    accessorKey: 'Type',
    header: 'Type',
  },
  {
    accessorKey: 'Created',
    header: 'Created',
  },
  {
    accessorKey: 'By',
    header: 'By',
  },
  {
    accessorKey: 'ToggleButton',
    header: 'ToggleButton ',
  },
];
