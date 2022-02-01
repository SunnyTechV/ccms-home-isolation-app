import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IsolationsDetailsComponent } from '../list/isolations-details.component';
import { IsolationsDetailsDetailComponent } from '../detail/isolations-details-detail.component';
import { IsolationsDetailsUpdateComponent } from '../update/isolations-details-update.component';
import { IsolationsDetailsRoutingResolveService } from './isolations-details-routing-resolve.service';

const isolationsDetailsRoute: Routes = [
  {
    path: '',
    component: IsolationsDetailsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IsolationsDetailsDetailComponent,
    resolve: {
      isolationsDetails: IsolationsDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IsolationsDetailsUpdateComponent,
    resolve: {
      isolationsDetails: IsolationsDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IsolationsDetailsUpdateComponent,
    resolve: {
      isolationsDetails: IsolationsDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(isolationsDetailsRoute)],
  exports: [RouterModule],
})
export class IsolationsDetailsRoutingModule {}
