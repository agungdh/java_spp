/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

/**
 *
 * @author user
 */
@Table("kelas")
@BelongsTo(parent = SiswaModel.class, foreignKeyName = "id_siswa")
public class KelasModel extends Model {}