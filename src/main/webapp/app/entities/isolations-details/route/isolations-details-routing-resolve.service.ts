import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIsolationsDetails, IsolationsDetails } from '../isolations-details.model';
import { IsolationsDetailsService } from '../service/isolations-details.service';

@Injectable({ providedIn: 'root' })
export class IsolationsDetailsRoutingResolveService implements Resolve<IIsolationsDetails> {
  constructor(protected service: IsolationsDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIsolationsDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((isolationsDetails: HttpResponse<IsolationsDetails>) => {
          if (isolationsDetails.body) {
            return of(isolationsDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IsolationsDetails());
  }
}
