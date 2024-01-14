import {Links} from "./links";

export class Client {
  constructor(
    public name: string,
    public clientId: number,
    public _links: Links

  ) {}
}
