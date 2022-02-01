import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssessmentAnswers, getAssessmentAnswersIdentifier } from '../assessment-answers.model';

export type EntityResponseType = HttpResponse<IAssessmentAnswers>;
export type EntityArrayResponseType = HttpResponse<IAssessmentAnswers[]>;

@Injectable({ providedIn: 'root' })
export class AssessmentAnswersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/assessment-answers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assessmentAnswers: IAssessmentAnswers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assessmentAnswers);
    return this.http
      .post<IAssessmentAnswers>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assessmentAnswers: IAssessmentAnswers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assessmentAnswers);
    return this.http
      .put<IAssessmentAnswers>(`${this.resourceUrl}/${getAssessmentAnswersIdentifier(assessmentAnswers) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assessmentAnswers: IAssessmentAnswers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assessmentAnswers);
    return this.http
      .patch<IAssessmentAnswers>(`${this.resourceUrl}/${getAssessmentAnswersIdentifier(assessmentAnswers) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssessmentAnswers>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssessmentAnswers[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssessmentAnswersToCollectionIfMissing(
    assessmentAnswersCollection: IAssessmentAnswers[],
    ...assessmentAnswersToCheck: (IAssessmentAnswers | null | undefined)[]
  ): IAssessmentAnswers[] {
    const assessmentAnswers: IAssessmentAnswers[] = assessmentAnswersToCheck.filter(isPresent);
    if (assessmentAnswers.length > 0) {
      const assessmentAnswersCollectionIdentifiers = assessmentAnswersCollection.map(
        assessmentAnswersItem => getAssessmentAnswersIdentifier(assessmentAnswersItem)!
      );
      const assessmentAnswersToAdd = assessmentAnswers.filter(assessmentAnswersItem => {
        const assessmentAnswersIdentifier = getAssessmentAnswersIdentifier(assessmentAnswersItem);
        if (assessmentAnswersIdentifier == null || assessmentAnswersCollectionIdentifiers.includes(assessmentAnswersIdentifier)) {
          return false;
        }
        assessmentAnswersCollectionIdentifiers.push(assessmentAnswersIdentifier);
        return true;
      });
      return [...assessmentAnswersToAdd, ...assessmentAnswersCollection];
    }
    return assessmentAnswersCollection;
  }

  protected convertDateFromClient(assessmentAnswers: IAssessmentAnswers): IAssessmentAnswers {
    return Object.assign({}, assessmentAnswers, {
      lastModified: assessmentAnswers.lastModified?.isValid() ? assessmentAnswers.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assessmentAnswers: IAssessmentAnswers) => {
        assessmentAnswers.lastModified = assessmentAnswers.lastModified ? dayjs(assessmentAnswers.lastModified) : undefined;
      });
    }
    return res;
  }
}
