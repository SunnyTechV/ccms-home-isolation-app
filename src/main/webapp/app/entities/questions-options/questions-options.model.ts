import dayjs from 'dayjs/esm';
import { IQuestions } from 'app/entities/questions/questions.model';
import { HealthCondition } from 'app/entities/enumerations/health-condition.model';

export interface IQuestionsOptions {
  id?: number;
  ansOption?: string | null;
  healthCondition?: HealthCondition | null;
  active?: boolean | null;
  lastModified?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  questions?: IQuestions | null;
}

export class QuestionsOptions implements IQuestionsOptions {
  constructor(
    public id?: number,
    public ansOption?: string | null,
    public healthCondition?: HealthCondition | null,
    public active?: boolean | null,
    public lastModified?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public questions?: IQuestions | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getQuestionsOptionsIdentifier(questionsOptions: IQuestionsOptions): number | undefined {
  return questionsOptions.id;
}
