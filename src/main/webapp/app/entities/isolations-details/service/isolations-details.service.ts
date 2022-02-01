import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIsolationsDetails, getIsolationsDetailsIdentifier } from '../isolations-details.model';

export type EntityResponseType = HttpResponse<IIsolationsDetails>;
export type EntityArrayResponseType = HttpResponse<IIsolationsDetails[]>;

@Injectable({ providedIn: 'root' })
export class IsolationsDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/isolations-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(isolationsDetails: IIsolationsDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isolationsDetails);
    return this.http
      .post<IIsolationsDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(isolationsDetails: IIsolationsDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isolationsDetails);
    return this.http
      .put<IIsolationsDetails>(`${this.resourceUrl}/${getIsolationsDetailsIdentifier(isolationsDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(isolationsDetails: IIsolationsDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isolationsDetails);
    return this.http
      .patch<IIsolationsDetails>(`${this.resourceUrl}/${getIsolationsDetailsIdentifier(isolationsDetails) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIsolationsDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIsolationsDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIsolationsDetailsToCollectionIfMissing(
    isolationsDetailsCollection: IIsolationsDetails[],
    ...isolationsDetailsToCheck: (IIsolationsDetails | null | undefined)[]
  ): IIsolationsDetails[] {
    const isolationsDetails: IIsolationsDetails[] = isolationsDetailsToCheck.filter(isPresent);
    if (isolationsDetails.length > 0) {
      const isolationsDetailsCollectionIdentifiers = isolationsDetailsCollection.map(
        isolationsDetailsItem => getIsolationsDetailsIdentifier(isolationsDetailsItem)!
      );
      const isolationsDetailsToAdd = isolationsDetails.filter(isolationsDetailsItem => {
        const isolationsDetailsIdentifier = getIsolationsDetailsIdentifier(isolationsDetailsItem);
        if (isolationsDetailsIdentifier == null || isolationsDetailsCollectionIdentifiers.includes(isolationsDetailsIdentifier)) {
          return false;
        }
        isolationsDetailsCollectionIdentifiers.push(isolationsDetailsIdentifier);
        return true;
      });
      return [...isolationsDetailsToAdd, ...isolationsDetailsCollection];
    }
    return isolationsDetailsCollection;
  }

  protected convertDateFromClient(isolationsDetails: IIsolationsDetails): IIsolationsDetails {
    return Object.assign({}, isolationsDetails, {
      isolationStartDate: isolationsDetails.isolationStartDate?.isValid() ? isolationsDetails.isolationStartDate.toJSON() : undefined,
      isolationEndDate: isolationsDetails.isolationEndDate?.isValid() ? isolationsDetails.isolationEndDate.toJSON() : undefined,
      lastAssessment: isolationsDetails.lastAssessment?.isValid() ? isolationsDetails.lastAssessment.toJSON() : undefined,
      lastModified: isolationsDetails.lastModified?.isValid() ? isolationsDetails.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.isolationStartDate = res.body.isolationStartDate ? dayjs(res.body.isolationStartDate) : undefined;
      res.body.isolationEndDate = res.body.isolationEndDate ? dayjs(res.body.isolationEndDate) : undefined;
      res.body.lastAssessment = res.body.lastAssessment ? dayjs(res.body.lastAssessment) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((isolationsDetails: IIsolationsDetails) => {
        isolationsDetails.isolationStartDate = isolationsDetails.isolationStartDate
          ? dayjs(isolationsDetails.isolationStartDate)
          : undefined;
        isolationsDetails.isolationEndDate = isolationsDetails.isolationEndDate ? dayjs(isolationsDetails.isolationEndDate) : undefined;
        isolationsDetails.lastAssessment = isolationsDetails.lastAssessment ? dayjs(isolationsDetails.lastAssessment) : undefined;
        isolationsDetails.lastModified = isolationsDetails.lastModified ? dayjs(isolationsDetails.lastModified) : undefined;
      });
    }
    return res;
  }
}
