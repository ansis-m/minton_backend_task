export class Transfer{

  constructor(
    public sourceId: number,
    public targetId: number,
    public amount: number,
    public currency: string
  ) {
  }
}
