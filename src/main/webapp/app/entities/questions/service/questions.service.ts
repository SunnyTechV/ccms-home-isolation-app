import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestions, getQuestionsIdentifier } from '../questions.model';

export type EntityResponseType = HttpResponse<IQuestions>;
export type EntityArrayResponseType = HttpResponse<IQuestions[]>;

@Injectable({ providedIn: 'root' })
export class QuestionsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/questions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questions: IQuestions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(questions);
    return this.http
      .post<IQuestions>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(questions: IQuestions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(questions);
    return this.http
      .put<IQuestions>(`${this.resourceUrl}/${getQuestionsIdentifier(questions) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(questions: IQuestions): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(questions);
    return this.http
      .patch<IQuestions>(`${this.resourceUrl}/${getQuestionsIdentifier(questions) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IQuestions>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IQuestions[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuestionsToCollectionIfMissing(
    questionsCollection: IQuestions[],
    ...questionsToCheck: (IQuestions | null | undefined)[]
  ): IQuestions[] {
    const questions: IQuestions[] = questionsToCheck.filter(isPresent);
    if (questions.length > 0) {
      const questionsCollectionIdentifiers = questionsCollection.map(questionsItem => getQuestionsIdentifier(questionsItem)!);
      const questionsToAdd = questions.filter(questionsItem => {
        const questionsIdentifier = getQuestionsIdentifier(questionsItem);
        if (questionsIdentifier == null || questionsCollectionIdentifiers.includes(questionsIdentifier)) {
          return false;
        }
        questionsCollectionIdentifiers.push(questionsIdentifier);
        return true;
      });
      return [...questionsToAdd, ...questionsCollection];
    }
    return questionsCollection;
  }

  protected convertDateFromClient(questions: IQuestions): IQuestions {
    return Object.assign({}, questions, {
      lastModified: questions.lastModified?.isValid() ? questions.lastModified.toJSON() : undefined,
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
      res.body.forEach((questions: IQuestions) => {
        questions.lastModified = questions.lastModified ? dayjs(questions.lastModified) : undefined;
      });
    }
    return res;
  }
}
