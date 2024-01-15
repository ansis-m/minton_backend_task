import {Account} from "./account";

export class Transaction{

  constructor(
    public transactionId: number,
    public accountFrom: Account | null,
    public accountTo: Account | null,
    public amountFrom: number | null,
    public amountTo: number | null,
    public conversionRate: number,
    public createdAt: string
  ) {}

}
