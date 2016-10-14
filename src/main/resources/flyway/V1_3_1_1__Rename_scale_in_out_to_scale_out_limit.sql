ALTER TABLE application ADD COLUMN scale_out_limit int;
UPDATE application SET scale_out_limit=(select scale_in_out  where application.id=application.id);
ALTER TABLE application DROP COLUMN scale_in_out;