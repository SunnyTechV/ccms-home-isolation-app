import dayjs from 'dayjs/esm';
import { IHomeIsolations } from 'app/entities/home-isolations/home-isolations.model';

export interface IIsolationsDetails {
  id?: number;
  isolationStartDate?: dayjs.Dayjs | null;
  isolationEndDate?: dayjs.Dayjs | null;
  referredDrName?: string | null;
  referredDrMobile?: string | null;
  prescriptionUrl?: string | null;
  reportUrl?: string | null;
  remarks?: string | null;
  selfRegistered?: boolean | null;
  lastAssessment?: dayjs.Dayjs | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  homeIsolations?: IHomeIsolations | null;
}

export class IsolationsDetails implements IIsolationsDetails {
  constructor(
    public id?: number,
    public isolationStartDate?: dayjs.Dayjs | null,
    public isolationEndDate?: dayjs.Dayjs | null,
    public referredDrName?: string | null,
    public referredDrMobile?: string | null,
    public prescriptionUrl?: string | null,
    public reportUrl?: string | null,
    public remarks?: string | null,
    public selfRegistered?: boolean | null,
    public lastAssessment?: dayjs.Dayjs | null,
    public lastModified?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public homeIsolations?: IHomeIsolations | null
  ) {
    this.selfRegistered = this.selfRegistered ?? false;
  }
}

export function getIsolationsDetailsIdentifier(isolationsDetails: IIsolationsDetails): number | undefined {
  return isolationsDetails.id;
}
