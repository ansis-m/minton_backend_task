import {Links} from "./links";
import {Account} from "./account";

export class Client {
  constructor(
    public name: string,
    public id: number,
    public accounts: Account[] = [],
    public selectedAccount: Account | undefined = undefined

  ) {}
}
