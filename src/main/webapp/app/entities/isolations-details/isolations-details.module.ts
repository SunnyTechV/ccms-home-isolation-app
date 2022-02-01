import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IsolationsDetailsComponent } from './list/isolations-details.component';
import { IsolationsDetailsDetailComponent } from './detail/isolations-details-detail.component';
import { IsolationsDetailsUpdateComponent } from './update/isolations-details-update.component';
import { IsolationsDetailsDeleteDialogComponent } from './delete/isolations-details-delete-dialog.component';
import { IsolationsDetailsRoutingModule } from './route/isolations-details-routing.module';

@NgModule({
  imports: [SharedModule, IsolationsDetailsRoutingModule],
  declarations: [
    IsolationsDetailsComponent,
    IsolationsDetailsDetailComponent,
    IsolationsDetailsUpdateComponent,
    IsolationsDetailsDeleteDialogComponent,
  ],
  entryComponents: [IsolationsDetailsDeleteDialogComponent],
})
export class IsolationsDetailsModule {}
