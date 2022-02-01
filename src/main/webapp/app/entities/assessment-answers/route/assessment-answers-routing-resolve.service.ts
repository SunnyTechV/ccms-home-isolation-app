import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssessmentAnswers, AssessmentAnswers } from '../assessment-answers.model';
import { AssessmentAnswersService } from '../service/assessment-answers.service';

@Injectable({ providedIn: 'root' })
export class AssessmentAnswersRoutingResolveService implements Resolve<IAssessmentAnswers> {
  constructor(protected service: AssessmentAnswersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssessmentAnswers> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assessmentAnswers: HttpResponse<AssessmentAnswers>) => {
          if (assessmentAnswers.body) {
            return of(assessmentAnswers.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssessmentAnswers());
  }
}
