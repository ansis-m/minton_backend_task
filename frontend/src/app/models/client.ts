import {Links} from "./links";
import {Account} from "./account";

export class Client {
  constructor(
    public name: string,
    public clientId: number,
    public _links: Links,
    public accounts: Account[] = [],
    public selectedAccount: Account | undefined = undefined

  ) {}
}
