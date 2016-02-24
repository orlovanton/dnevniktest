package ru.antonorlov.dnevniktest.exception;

/**
 * Created by antonorlov on 24/02/16.
 */

/**
 * Ошибки при обработке документа
 */
public class DocumentError {

    private String message;

    public DocumentError(Message message) {
        this.message = message.getText();
    }

    public DocumentError(Message message, int rowNum) {
        this.message = String.format(message.getText(), rowNum);
    }

    public DocumentError(Message message, int rowNum, int cellNum) {
        this.message = String.format(message.getText(), rowNum, cellNum);
    }

    public String getMessage() {
        return message;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DocumentError{");
        sb.append(", text='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }


    /**
     * user-friendly ошибки. На UI передаются сообщения
     */
    public enum Message {
        GENERAL_ERROR("Поизошла ошибка загрузки"),
        WRONG_FILE_FORMAT_ERROR("Неверный формате док-та. Допускаются только Excel файлы с расшиением .xlsx"),
        EMPTY_OR_WRONG_FORMATED_DOCUMENT_ERROR("Документ пуст или имеет неверный формат данных"),
        DOCUMENT_PROCESSING_ERROR("Ошибка обработки документа"),
        ROW_ERROR("Ошибка в стороке %s"),
        CELL_ERROR("Ошибка в строке %s столбце %s");

        private String text;

        Message(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
