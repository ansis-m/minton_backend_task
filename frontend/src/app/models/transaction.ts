import {Account} from "./account";

export class Transaction{

  constructor(
    public transactionId: number,
    public accountFrom: Account | null,
    public accountTo: Account | null,
    public ammountFrom: number | null,
    public ammountTo: number | null,
    public conversionrate: number,
    public createdAt: string
  ) {}

}
