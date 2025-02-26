export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  pageSize: number;
  page: number;
}
