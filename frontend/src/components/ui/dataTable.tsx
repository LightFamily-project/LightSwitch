'use client';
import {
  ColumnDef,
  SortingState,
  flexRender,
  getCoreRowModel,
  getPaginationRowModel,
  useReactTable,
  getSortedRowModel,
  Row,
  PaginationState,
} from '@tanstack/react-table';
import { SetStateAction, useState } from 'react';
import { Trash2 } from 'lucide-react';

import { Switch } from './switch';

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { Button } from '@/components/ui/button';
import { TableDataType } from '@/types/home';

export interface DataTableProps<TData extends TableDataType, TValue> {
  columns: ColumnDef<TData, TValue>[];
  data: TData[];
  setData: React.Dispatch<SetStateAction<TData[]>>;
}

export function DataTable<TData extends TableDataType, TValue>({
  columns,
  data,
  setData,
}: DataTableProps<TData, TValue>) {
  const [sorting, setSorting] = useState<SortingState>([]);
  const [rowSelection, setRowSelection] = useState({});
  const [pagination, setPagination] = useState<PaginationState>({
    pageIndex: 0,
    pageSize: 9,
  });

  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    onRowSelectionChange: setRowSelection,
    onSortingChange: setSorting,
    getSortedRowModel: getSortedRowModel(),
    state: {
      rowSelection,
      sorting,
      pagination,
    },
    onPaginationChange: (updater) => {
      setPagination((prev) => ({
        ...prev,
        ...updater,
      }));
    },
  });

  const handleStatusChange = (row: Row<TableDataType>) => {
    const newData = data.map((item) => {
      if (item.Name === row.getValue('Name')) {
        return { ...item, Status: !item.Status };
      }
      return item;
    });
    setData(newData);
  };

  const removeRow = (rowId: number) => {
    setData((prevData) => prevData.filter((_row, i) => i !== rowId));
  };

  const handlePreviousPage = () => {
    setPagination((prev) => ({
      ...prev,
      pageIndex: Math.max(prev.pageIndex - 1, 0),
    }));
  };

  const handleNextPage = () => {
    setPagination((prev) => ({
      ...prev,
      pageIndex: prev.pageIndex + 1,
    }));
  };

  return (
    <div>
      <div className="w-full rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => {
                  return (
                    <TableHead key={header.id}>
                      {header.isPlaceholder
                        ? null
                        : flexRender(
                            header.column.columnDef.header,
                            header.getContext(),
                          )}
                    </TableHead>
                  );
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow
                  key={row.id}
                  data-state={row.getIsSelected() && 'selected'}
                >
                  {row.getVisibleCells().map((cell) => {
                    if (cell.column.id === 'Status') {
                      return (
                        <TableCell key={cell.id}>
                          <Switch
                            checked={row.getValue('Status')}
                            onCheckedChange={() => handleStatusChange(row)}
                          />
                        </TableCell>
                      );
                    }
                    return (
                      <TableCell key={cell.id}>
                        {flexRender(
                          cell.column.columnDef.cell,
                          cell.getContext(),
                        )}
                      </TableCell>
                    );
                  })}
                  <TableCell>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => removeRow(row.index)}
                    >
                      <Trash2 className="size-4" />
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell
                  colSpan={columns.length}
                  className="h-24 text-center"
                >
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div className="flex w-full items-center justify-between space-x-2 py-4">
        {table.getFilteredSelectedRowModel().rows.length} of{' '}
        {table.getFilteredRowModel().rows.length} row(s) selected
        <div className="flex w-[220px] flex-row items-center justify-between">
          <span className="text-[0.8rem]">
            Page {pagination.pageIndex + 1} of {table.getPageCount()}
          </span>
          <div className="flex w-[135px] flex-row items-center justify-between">
            <Button
              variant="outline"
              size="sm"
              onClick={handlePreviousPage}
              disabled={pagination.pageIndex === 0}
            >
              Previous
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={handleNextPage}
              disabled={pagination.pageIndex >= table.getPageCount() - 1}
            >
              Next
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
