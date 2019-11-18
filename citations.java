// ORM class for table 'citations'
// WARNING: This class is AUTO-GENERATED. Modify at your own risk.
//
// Debug information:
// Generated date: Thu Jul 18 14:18:58 PDT 2019
// For connector: org.apache.sqoop.manager.MySQLManager
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBWritable;
import com.cloudera.sqoop.lib.JdbcWritableBridge;
import com.cloudera.sqoop.lib.DelimiterSet;
import com.cloudera.sqoop.lib.FieldFormatter;
import com.cloudera.sqoop.lib.RecordParser;
import com.cloudera.sqoop.lib.BooleanParser;
import com.cloudera.sqoop.lib.BlobRef;
import com.cloudera.sqoop.lib.ClobRef;
import com.cloudera.sqoop.lib.LargeObjectLoader;
import com.cloudera.sqoop.lib.SqoopRecord;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class citations extends SqoopRecord  implements DBWritable, Writable {
  private final int PROTOCOL_VERSION = 3;
  public int getClassFormatVersion() { return PROTOCOL_VERSION; }
  public static interface FieldSetterCommand {    void setField(Object value);  }  protected ResultSet __cur_result_set;
  private Map<String, FieldSetterCommand> setters = new HashMap<String, FieldSetterCommand>();
  private void init0() {
    setters.put("date", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        date = (String)value;
      }
    });
    setters.put("time", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        time = (String)value;
      }
    });
    setters.put("address", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        address = (String)value;
      }
    });
    setters.put("citation_code", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        citation_code = (String)value;
      }
    });
    setters.put("citation", new FieldSetterCommand() {
      @Override
      public void setField(Object value) {
        citation = (String)value;
      }
    });
  }
  public citations() {
    init0();
  }
  private String date;
  public String get_date() {
    return date;
  }
  public void set_date(String date) {
    this.date = date;
  }
  public citations with_date(String date) {
    this.date = date;
    return this;
  }
  private String time;
  public String get_time() {
    return time;
  }
  public void set_time(String time) {
    this.time = time;
  }
  public citations with_time(String time) {
    this.time = time;
    return this;
  }
  private String address;
  public String get_address() {
    return address;
  }
  public void set_address(String address) {
    this.address = address;
  }
  public citations with_address(String address) {
    this.address = address;
    return this;
  }
  private String citation_code;
  public String get_citation_code() {
    return citation_code;
  }
  public void set_citation_code(String citation_code) {
    this.citation_code = citation_code;
  }
  public citations with_citation_code(String citation_code) {
    this.citation_code = citation_code;
    return this;
  }
  private String citation;
  public String get_citation() {
    return citation;
  }
  public void set_citation(String citation) {
    this.citation = citation;
  }
  public citations with_citation(String citation) {
    this.citation = citation;
    return this;
  }
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof citations)) {
      return false;
    }
    citations that = (citations) o;
    boolean equal = true;
    equal = equal && (this.date == null ? that.date == null : this.date.equals(that.date));
    equal = equal && (this.time == null ? that.time == null : this.time.equals(that.time));
    equal = equal && (this.address == null ? that.address == null : this.address.equals(that.address));
    equal = equal && (this.citation_code == null ? that.citation_code == null : this.citation_code.equals(that.citation_code));
    equal = equal && (this.citation == null ? that.citation == null : this.citation.equals(that.citation));
    return equal;
  }
  public boolean equals0(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof citations)) {
      return false;
    }
    citations that = (citations) o;
    boolean equal = true;
    equal = equal && (this.date == null ? that.date == null : this.date.equals(that.date));
    equal = equal && (this.time == null ? that.time == null : this.time.equals(that.time));
    equal = equal && (this.address == null ? that.address == null : this.address.equals(that.address));
    equal = equal && (this.citation_code == null ? that.citation_code == null : this.citation_code.equals(that.citation_code));
    equal = equal && (this.citation == null ? that.citation == null : this.citation.equals(that.citation));
    return equal;
  }
  public void readFields(ResultSet __dbResults) throws SQLException {
    this.__cur_result_set = __dbResults;
    this.date = JdbcWritableBridge.readString(1, __dbResults);
    this.time = JdbcWritableBridge.readString(2, __dbResults);
    this.address = JdbcWritableBridge.readString(3, __dbResults);
    this.citation_code = JdbcWritableBridge.readString(4, __dbResults);
    this.citation = JdbcWritableBridge.readString(5, __dbResults);
  }
  public void readFields0(ResultSet __dbResults) throws SQLException {
    this.date = JdbcWritableBridge.readString(1, __dbResults);
    this.time = JdbcWritableBridge.readString(2, __dbResults);
    this.address = JdbcWritableBridge.readString(3, __dbResults);
    this.citation_code = JdbcWritableBridge.readString(4, __dbResults);
    this.citation = JdbcWritableBridge.readString(5, __dbResults);
  }
  public void loadLargeObjects(LargeObjectLoader __loader)
      throws SQLException, IOException, InterruptedException {
  }
  public void loadLargeObjects0(LargeObjectLoader __loader)
      throws SQLException, IOException, InterruptedException {
  }
  public void write(PreparedStatement __dbStmt) throws SQLException {
    write(__dbStmt, 0);
  }

  public int write(PreparedStatement __dbStmt, int __off) throws SQLException {
    JdbcWritableBridge.writeString(date, 1 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeString(time, 2 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeString(address, 3 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeString(citation_code, 4 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeString(citation, 5 + __off, 12, __dbStmt);
    return 5;
  }
  public void write0(PreparedStatement __dbStmt, int __off) throws SQLException {
    JdbcWritableBridge.writeString(date, 1 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeString(time, 2 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeString(address, 3 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeString(citation_code, 4 + __off, 12, __dbStmt);
    JdbcWritableBridge.writeString(citation, 5 + __off, 12, __dbStmt);
  }
  public void readFields(DataInput __dataIn) throws IOException {
this.readFields0(__dataIn);  }
  public void readFields0(DataInput __dataIn) throws IOException {
    if (__dataIn.readBoolean()) { 
        this.date = null;
    } else {
    this.date = Text.readString(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.time = null;
    } else {
    this.time = Text.readString(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.address = null;
    } else {
    this.address = Text.readString(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.citation_code = null;
    } else {
    this.citation_code = Text.readString(__dataIn);
    }
    if (__dataIn.readBoolean()) { 
        this.citation = null;
    } else {
    this.citation = Text.readString(__dataIn);
    }
  }
  public void write(DataOutput __dataOut) throws IOException {
    if (null == this.date) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, date);
    }
    if (null == this.time) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, time);
    }
    if (null == this.address) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, address);
    }
    if (null == this.citation_code) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, citation_code);
    }
    if (null == this.citation) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, citation);
    }
  }
  public void write0(DataOutput __dataOut) throws IOException {
    if (null == this.date) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, date);
    }
    if (null == this.time) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, time);
    }
    if (null == this.address) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, address);
    }
    if (null == this.citation_code) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, citation_code);
    }
    if (null == this.citation) { 
        __dataOut.writeBoolean(true);
    } else {
        __dataOut.writeBoolean(false);
    Text.writeString(__dataOut, citation);
    }
  }
  private static final DelimiterSet __outputDelimiters = new DelimiterSet((char) 44, (char) 10, (char) 0, (char) 0, false);
  public String toString() {
    return toString(__outputDelimiters, true);
  }
  public String toString(DelimiterSet delimiters) {
    return toString(delimiters, true);
  }
  public String toString(boolean useRecordDelim) {
    return toString(__outputDelimiters, useRecordDelim);
  }
  public String toString(DelimiterSet delimiters, boolean useRecordDelim) {
    StringBuilder __sb = new StringBuilder();
    char fieldDelim = delimiters.getFieldsTerminatedBy();
    __sb.append(FieldFormatter.escapeAndEnclose(date==null?"null":date, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(time==null?"null":time, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(address==null?"null":address, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(citation_code==null?"null":citation_code, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(citation==null?"null":citation, delimiters));
    if (useRecordDelim) {
      __sb.append(delimiters.getLinesTerminatedBy());
    }
    return __sb.toString();
  }
  public void toString0(DelimiterSet delimiters, StringBuilder __sb, char fieldDelim) {
    __sb.append(FieldFormatter.escapeAndEnclose(date==null?"null":date, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(time==null?"null":time, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(address==null?"null":address, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(citation_code==null?"null":citation_code, delimiters));
    __sb.append(fieldDelim);
    __sb.append(FieldFormatter.escapeAndEnclose(citation==null?"null":citation, delimiters));
  }
  private static final DelimiterSet __inputDelimiters = new DelimiterSet((char) 44, (char) 10, (char) 0, (char) 0, false);
  private RecordParser __parser;
  public void parse(Text __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(CharSequence __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(byte [] __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(char [] __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(ByteBuffer __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  public void parse(CharBuffer __record) throws RecordParser.ParseError {
    if (null == this.__parser) {
      this.__parser = new RecordParser(__inputDelimiters);
    }
    List<String> __fields = this.__parser.parseRecord(__record);
    __loadFromFields(__fields);
  }

  private void __loadFromFields(List<String> fields) {
    Iterator<String> __it = fields.listIterator();
    String __cur_str = null;
    try {
    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.date = null; } else {
      this.date = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.time = null; } else {
      this.time = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.address = null; } else {
      this.address = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.citation_code = null; } else {
      this.citation_code = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.citation = null; } else {
      this.citation = __cur_str;
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  private void __loadFromFields0(Iterator<String> __it) {
    String __cur_str = null;
    try {
    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.date = null; } else {
      this.date = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.time = null; } else {
      this.time = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.address = null; } else {
      this.address = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.citation_code = null; } else {
      this.citation_code = __cur_str;
    }

    __cur_str = __it.next();
    if (__cur_str.equals("null")) { this.citation = null; } else {
      this.citation = __cur_str;
    }

    } catch (RuntimeException e) {    throw new RuntimeException("Can't parse input data: '" + __cur_str + "'", e);    }  }

  public Object clone() throws CloneNotSupportedException {
    citations o = (citations) super.clone();
    return o;
  }

  public void clone0(citations o) throws CloneNotSupportedException {
  }

  public Map<String, Object> getFieldMap() {
    Map<String, Object> __sqoop$field_map = new HashMap<String, Object>();
    __sqoop$field_map.put("date", this.date);
    __sqoop$field_map.put("time", this.time);
    __sqoop$field_map.put("address", this.address);
    __sqoop$field_map.put("citation_code", this.citation_code);
    __sqoop$field_map.put("citation", this.citation);
    return __sqoop$field_map;
  }

  public void getFieldMap0(Map<String, Object> __sqoop$field_map) {
    __sqoop$field_map.put("date", this.date);
    __sqoop$field_map.put("time", this.time);
    __sqoop$field_map.put("address", this.address);
    __sqoop$field_map.put("citation_code", this.citation_code);
    __sqoop$field_map.put("citation", this.citation);
  }

  public void setField(String __fieldName, Object __fieldVal) {
    if (!setters.containsKey(__fieldName)) {
      throw new RuntimeException("No such field:"+__fieldName);
    }
    setters.get(__fieldName).setField(__fieldVal);
  }

}
