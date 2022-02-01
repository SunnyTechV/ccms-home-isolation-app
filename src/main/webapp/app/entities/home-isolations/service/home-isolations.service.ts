import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHomeIsolations, getHomeIsolationsIdentifier } from '../home-isolations.model';

export type EntityResponseType = HttpResponse<IHomeIsolations>;
export type EntityArrayResponseType = HttpResponse<IHomeIsolations[]>;

@Injectable({ providedIn: 'root' })
export class HomeIsolationsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/home-isolations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(homeIsolations: IHomeIsolations): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(homeIsolations);
    return this.http
      .post<IHomeIsolations>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(homeIsolations: IHomeIsolations): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(homeIsolations);
    return this.http
      .put<IHomeIsolations>(`${this.resourceUrl}/${getHomeIsolationsIdentifier(homeIsolations) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(homeIsolations: IHomeIsolations): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(homeIsolations);
    return this.http
      .patch<IHomeIsolations>(`${this.resourceUrl}/${getHomeIsolationsIdentifier(homeIsolations) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHomeIsolations>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHomeIsolations[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHomeIsolationsToCollectionIfMissing(
    homeIsolationsCollection: IHomeIsolations[],
    ...homeIsolationsToCheck: (IHomeIsolations | null | undefined)[]
  ): IHomeIsolations[] {
    const homeIsolations: IHomeIsolations[] = homeIsolationsToCheck.filter(isPresent);
    if (homeIsolations.length > 0) {
      const homeIsolationsCollectionIdentifiers = homeIsolationsCollection.map(
        homeIsolationsItem => getHomeIsolationsIdentifier(homeIsolationsItem)!
      );
      const homeIsolationsToAdd = homeIsolations.filter(homeIsolationsItem => {
        const homeIsolationsIdentifier = getHomeIsolationsIdentifier(homeIsolationsItem);
        if (homeIsolationsIdentifier == null || homeIsolationsCollectionIdentifiers.includes(homeIsolationsIdentifier)) {
          return false;
        }
        homeIsolationsCollectionIdentifiers.push(homeIsolationsIdentifier);
        return true;
      });
      return [...homeIsolationsToAdd, ...homeIsolationsCollection];
    }
    return homeIsolationsCollection;
  }

  protected convertDateFromClient(homeIsolations: IHomeIsolations): IHomeIsolations {
    return Object.assign({}, homeIsolations, {
      collectionDate: homeIsolations.collectionDate?.isValid() ? homeIsolations.collectionDate.toJSON() : undefined,
      hospitalizationDate: homeIsolations.hospitalizationDate?.isValid() ? homeIsolations.hospitalizationDate.toJSON() : undefined,
      lastModified: homeIsolations.lastModified?.isValid() ? homeIsolations.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.collectionDate = res.body.collectionDate ? dayjs(res.body.collectionDate) : undefined;
      res.body.hospitalizationDate = res.body.hospitalizationDate ? dayjs(res.body.hospitalizationDate) : undefined;
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((homeIsolations: IHomeIsolations) => {
        homeIsolations.collectionDate = homeIsolations.collectionDate ? dayjs(homeIsolations.collectionDate) : undefined;
        homeIsolations.hospitalizationDate = homeIsolations.hospitalizationDate ? dayjs(homeIsolations.hospitalizationDate) : undefined;
        homeIsolations.lastModified = homeIsolations.lastModified ? dayjs(homeIsolations.lastModified) : undefined;
      });
    }
    return res;
  }
}
