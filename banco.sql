#Creación de la base de datos
#Alumnos: Agustín Fiore Ibarguren y Guillermo Seewald

CREATE DATABASE banco;

use banco;

CREATE TABLE `cliente` (
  `nro_cliente` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `apellido` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `tipo_doc` varchar(20) NOT NULL,
  `nro_doc` int(8) unsigned NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `fecha_nac` date NOT NULL,
  PRIMARY KEY (`nro_cliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE caja (
  cod_caja int(5) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (cod_caja)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `ciudad` (
  `cod_postal` int(4) unsigned NOT NULL,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`cod_postal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `sucursal` (
  `nro_suc` int(3) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `horario` varchar(45) NOT NULL,
  `cod_postal` int(4) unsigned NOT NULL,
  PRIMARY KEY (`nro_suc`),
  KEY `fk_cod_postal_idx` (`cod_postal`),
  CONSTRAINT `fk_cod_postal` FOREIGN KEY (`cod_postal`) REFERENCES `ciudad` (`cod_postal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `empleado` (
  `legajo` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `apellido` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `tipo_doc` varchar(20) NOT NULL,
  `nro_doc` int(8) unsigned NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `cargo` varchar(45) NOT NULL,
  `password` char(32) NOT NULL,
  `nro_suc` int(3) unsigned NOT NULL,
  PRIMARY KEY (`legajo`),
  KEY `fk_nro_suc_idx` (`nro_suc`),
  CONSTRAINT `fk_nro_suc` FOREIGN KEY (`nro_suc`) REFERENCES `sucursal` (`nro_suc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transaccion` (
  `nro_trans` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `monto` decimal(16,2) unsigned NOT NULL,
  PRIMARY KEY (`nro_trans`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transaccion_por_caja` (
  `nro_trans` int(10) unsigned NOT NULL,
  `cod_caja` int(5) unsigned NOT NULL,
  CHECK (nro_trans >= 0),
  PRIMARY KEY (`nro_trans`),
  KEY `fk_transcaja_cod_caja_idx` (`cod_caja`),
  CONSTRAINT `fk_transcaja_cod_caja` FOREIGN KEY (`cod_caja`) REFERENCES `caja` (`cod_caja`),
  CONSTRAINT `fk_transcaja_nro_trans` FOREIGN KEY (`nro_trans`) REFERENCES `transaccion` (`nro_trans`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `prestamo` (
  `nro_prestamo` int(8) unsigned NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `cant_meses` int(2) unsigned NOT NULL,
  `monto` decimal(10,2) unsigned NOT NULL,
  `tasa_interes` decimal(4,2) unsigned NOT NULL,
  `interes` decimal(9,2) unsigned NOT NULL,
  `valor_cuota` decimal(9,2) unsigned NOT NULL,
  `legajo` int(4) unsigned NOT NULL,
  `nro_cliente` int(5) unsigned NOT NULL,
  PRIMARY KEY (`nro_prestamo`),
  KEY `fk_prestamo_legajo_idx` (`legajo`),
  KEY `fk_prestamo_nro_cliente_idx` (`nro_cliente`),
  CONSTRAINT `fk_prestamo_legajo` FOREIGN KEY (`legajo`) REFERENCES `empleado` (`legajo`),
  CONSTRAINT `fk_prestamo_nro_cliente` FOREIGN KEY (`nro_cliente`) REFERENCES `cliente` (`nro_cliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `plazo_fijo` (
  `nro_plazo` int(8) unsigned NOT NULL AUTO_INCREMENT,
  `capital` decimal(16,2) unsigned NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `tasa_interes` decimal(4,2) unsigned NOT NULL,
  `interes` decimal(16,2) unsigned NOT NULL,
  `nro_suc` int(3) unsigned NOT NULL,
  PRIMARY KEY (`nro_plazo`),
  KEY `fk_nro_suc_plazo_idx` (`nro_suc`),
  CONSTRAINT `fk_nro_suc_plazo` FOREIGN KEY (`nro_suc`) REFERENCES `sucursal` (`nro_suc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `atm` (
  `cod_caja` int(5) unsigned NOT NULL,
  `cod_postal` int(4) unsigned NOT NULL,
  `direccion` varchar(45) NOT NULL,
  PRIMARY KEY (`cod_caja`),
  KEY `fk_atm_cod_postal_idx` (`cod_postal`),
  CONSTRAINT `fk_atm_cod_caja` FOREIGN KEY (`cod_caja`) REFERENCES `caja` (`cod_caja`),
  CONSTRAINT `fk_atm_cod_postal` FOREIGN KEY (`cod_postal`) REFERENCES `ciudad` (`cod_postal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `caja_ahorro` (
  `nro_ca` int(8) unsigned NOT NULL AUTO_INCREMENT,
  `CBU` bigint(18) unsigned NOT NULL,
  `saldo` decimal(16,2) unsigned NOT NULL,
  PRIMARY KEY (`nro_ca`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `cliente_ca` (
  `nro_cliente` int(5) unsigned NOT NULL,
  `nro_ca` int(8) unsigned NOT NULL,
  PRIMARY KEY (`nro_cliente`,`nro_ca`),
  KEY `fk_clienteca_nro_ca_idx` (`nro_ca`),
  CONSTRAINT `fk_clienteca_nro_ca` FOREIGN KEY (`nro_ca`) REFERENCES `caja_ahorro` (`nro_ca`),
  CONSTRAINT `fk_clienteca_nro_cliente` FOREIGN KEY (`nro_cliente`) REFERENCES `cliente` (`nro_cliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `debito` (
  `nro_trans` int(10) unsigned NOT NULL,
  `descripcion` TINYTEXT DEFAULT NULL,
  `nro_cliente` int(5) unsigned NOT NULL,
  `nro_ca` int(8) unsigned NOT NULL,
  PRIMARY KEY (`nro_trans`),
  KEY `fk_debito_nro_cliente_ca_idx` (`nro_cliente`,`nro_ca`),
  CONSTRAINT `fk_debito_nro_cliente_ca` FOREIGN KEY (`nro_cliente`, `nro_ca`) REFERENCES `cliente_ca` (`nro_cliente`, `nro_ca`),
  CONSTRAINT `fk_debito_nro_trans` FOREIGN KEY (`nro_trans`) REFERENCES `transaccion` (`nro_trans`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `deposito` (
  `nro_trans` int(10) unsigned NOT NULL,
  `nro_ca` int(8) unsigned NOT NULL,
  PRIMARY KEY (`nro_trans`),
  KEY `fk_deposito_nro_ca_idx` (`nro_ca`),
  CONSTRAINT `fk_deposito_nro_ca` FOREIGN KEY (`nro_ca`) REFERENCES `caja_ahorro` (`nro_ca`),
  CONSTRAINT `fk_deposito_nro_trans` FOREIGN KEY (`nro_trans`) REFERENCES `transaccion_por_caja` (`nro_trans`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `extraccion` (
  `nro_trans` int(10) unsigned NOT NULL,
  `nro_cliente` int(5) unsigned NOT NULL,
  `nro_ca` int(8) unsigned NOT NULL,
  CHECK (nro_trans >= 0),
  PRIMARY KEY (`nro_trans`),
  KEY `fk_extraccion_nro_cliente_idx` (`nro_cliente`,`nro_ca`),
  CONSTRAINT `fk_extraccion_nro_cliente_ca` FOREIGN KEY (`nro_cliente`, `nro_ca`) REFERENCES `cliente_ca` (`nro_cliente`, `nro_ca`),
  CONSTRAINT `fk_extraccion_nro_trans` FOREIGN KEY (`nro_trans`) REFERENCES `transaccion_por_caja` (`nro_trans`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pago` (
  `nro_prestamo` int(8) unsigned NOT NULL,
  `nro_pago` int(2) unsigned NOT NULL,
  `fecha_venc` date NOT NULL,
  `fecha_pago` date DEFAULT NULL,
  PRIMARY KEY (`nro_prestamo`,`nro_pago`),
  CONSTRAINT `fk_pago_nro_prestamo` FOREIGN KEY (`nro_prestamo`) REFERENCES `prestamo` (`nro_prestamo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `plazo_cliente` (
  `nro_plazo` int(8) unsigned NOT NULL,
  `nro_cliente` int(5) unsigned NOT NULL,
  PRIMARY KEY (`nro_plazo`,`nro_cliente`),
  KEY `fk_pc_nro_cliente_idx` (`nro_cliente`),
  CONSTRAINT `fk_pc_nro_cliente` FOREIGN KEY (`nro_cliente`) REFERENCES `cliente` (`nro_cliente`),
  CONSTRAINT `fk_pc_nro_plazo` FOREIGN KEY (`nro_plazo`) REFERENCES `plazo_fijo` (`nro_plazo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tarjeta` (
  `nro_tarjeta` bigint(16) unsigned NOT NULL AUTO_INCREMENT,
  `PIN` varchar(32) NOT NULL,
  `CVT` varchar(32) NOT NULL,
  `fecha_venc` date NOT NULL,
  `nro_cliente` int(5) unsigned NOT NULL,
  `nro_ca` int(8) unsigned NOT NULL,
  PRIMARY KEY (`nro_tarjeta`),
  KEY `fk_tarjeta_nro_cliente_ca_idx` (`nro_cliente`,`nro_ca`),
  CONSTRAINT `fk_tarjeta_nro_cliente_ca` FOREIGN KEY (`nro_cliente`, `nro_ca`) REFERENCES `cliente_ca` (`nro_cliente`, `nro_ca`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tasa_prestamo` (
  `periodo` int(3) unsigned NOT NULL,
  `monto_inf` decimal(10,2) unsigned NOT NULL,
  `monto_sup` decimal(10,2) unsigned NOT NULL,
  `tasa` decimal(4,2) unsigned NOT NULL,
  PRIMARY KEY (`periodo`,`monto_inf`,`monto_sup`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tasa_plazo_fijo` (
  `periodo` int(3) unsigned NOT NULL,
  `monto_inf` decimal(16,2) unsigned NOT NULL,
  `monto_sup` decimal(16,2) unsigned NOT NULL,
  `tasa` decimal(4,2) unsigned NOT NULL,
  PRIMARY KEY (`periodo`,`monto_inf`,`monto_sup`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transferencia` (
  `nro_trans` int(10) unsigned NOT NULL,
  `nro_cliente` int(5) unsigned NOT NULL,
  `origen` int(8) unsigned NOT NULL,
  `destino` int(8) unsigned NOT NULL,
  PRIMARY KEY (`nro_trans`),
  KEY `fk_transferencia_nro_cliente_origen_idx` (`nro_cliente`,`origen`),
  KEY `fk_transferencia_destino_idx` (`destino`),
  CONSTRAINT `fk_transferencia_destino` FOREIGN KEY (`destino`) REFERENCES `caja_ahorro` (`nro_ca`),
  CONSTRAINT `fk_transferencia_nro_cliente_origen` FOREIGN KEY (`nro_cliente`, `origen`) REFERENCES `cliente_ca` (`nro_cliente`, `nro_ca`),
  CONSTRAINT `fk_transferencia_nro_trans` FOREIGN KEY (`nro_trans`) REFERENCES `transaccion_por_caja` (`nro_trans`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `ventanilla` (
  `cod_caja` int(5) unsigned NOT NULL,
  `nro_suc` int(3) unsigned NOT NULL,
  PRIMARY KEY (`cod_caja`),
  KEY `fk_ventanilla_nro_suc_idx` (`nro_suc`),
  CONSTRAINT `fk_ventanilla_cod_caja` FOREIGN KEY (`cod_caja`) REFERENCES `caja` (`cod_caja`),
  CONSTRAINT `fk_ventanilla_nro_suc` FOREIGN KEY (`nro_suc`) REFERENCES `sucursal` (`nro_suc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



#------------ Vista ------------------

CREATE 
VIEW `trans_cajas_ahorro` AS
    SELECT ca.nro_ca, saldo, t.nro_trans, fecha, hora, 'Debito' as tipo, monto, null as destino, null as cod_caja, c.nro_cliente, tipo_doc, nro_doc, nombre, apellido
	FROM ((transaccion t JOIN debito d ON t.nro_trans=d.nro_trans) JOIN cliente c ON d.nro_cliente=c.nro_cliente) JOIN caja_ahorro ca ON d.nro_ca=ca.nro_ca
	UNION
	SELECT ca.nro_ca, saldo, t.nro_trans, fecha, hora, 'Extraccion' as tipo, monto, null as destino, cod_caja, c.nro_cliente, tipo_doc, nro_doc, nombre, apellido
	FROM (((transaccion_por_caja tr JOIN extraccion e ON tr.nro_trans=e.nro_trans) JOIN transaccion t ON tr.nro_trans=t.nro_trans) JOIN cliente c ON e.nro_cliente=c.nro_cliente) JOIN caja_ahorro ca ON e.nro_ca=ca.nro_ca
	UNION
	SELECT ca.nro_ca, saldo, t.nro_trans, fecha, hora, 'Transferencia' as tipo, monto, destino, cod_caja, c.nro_cliente, tipo_doc, nro_doc, nombre, apellido
	FROM (((transaccion_por_caja tr JOIN transferencia ts ON tr.nro_trans=ts.nro_trans) JOIN transaccion t ON tr.nro_trans=t.nro_trans) JOIN cliente c ON ts.nro_cliente=c.nro_cliente) JOIN caja_ahorro ca ON origen=ca.nro_ca
	UNION
	SELECT ca.nro_ca, saldo, t.nro_trans, fecha, hora, 'Deposito' as tipo, monto, null as destino, cod_caja, null as nro_cliente, null as tipo_doc, null as nro_doc, null as nombre, null as apellido
	FROM ((transaccion_por_caja tr JOIN deposito d ON tr.nro_trans=d.nro_trans) JOIN transaccion t ON tr.nro_trans=t.nro_trans) JOIN caja_ahorro ca ON d.nro_ca=ca.nro_ca;


#------------- Stored procedures ------------------

delimiter !

CREATE PROCEDURE transferir(IN monto DECIMAL(16,2), IN cuentaA INT,
                            IN cuentaB INT, IN nro_clienteA INT, IN nro_caja INT)
                            
  BEGIN   
     # Declaro una variable local saldo_actual	
	 DECLARE saldo_actual_cuentaA DECIMAL(16,2);
	 DECLARE saldo_actual_cuentaB DECIMAL(16,2);
     
     # Declaro variables locales para recuperar los errores 
	 DECLARE codigo_SQL  CHAR(5) DEFAULT '00000';	 
	 DECLARE codigo_MYSQL INT DEFAULT 0;
	 DECLARE mensaje_error TEXT;
	 
     DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
	  BEGIN 
		GET DIAGNOSTICS CONDITION 1  codigo_MYSQL= MYSQL_ERRNO,  
		                             codigo_SQL= RETURNED_SQLSTATE, 
									 mensaje_error= MESSAGE_TEXT;
	    SELECT 'SQLEXCEPTION!, Transacción abortada.' AS resultado, 
		        codigo_MySQL, codigo_SQL,  mensaje_error;		
        ROLLBACK;
	  END;		      
         
	 START TRANSACTION;	# Comienza la transacción  
	   IF EXISTS (SELECT * FROM caja_ahorro WHERE nro_ca=cuentaA) AND
          EXISTS (SELECT * FROM caja_ahorro WHERE nro_ca=cuentaB)
	   THEN  # verifico que existan ambas cuentas
		  SELECT saldo INTO saldo_actual_cuentaA
		  FROM caja_ahorro WHERE nro_ca=cuentaA FOR UPDATE;
		  # Recupero el saldo de la cuentaA en la variable saldo_actual_cuentaA.
		  SELECT saldo INTO saldo_actual_cuentaB
		  FROM caja_ahorro WHERE nro_ca=cuentaB FOR UPDATE;
		  # Recupero el saldo de la cuentaB en la variable saldo_actual_cuentaB.      	    
      
	      IF saldo_actual_cuentaA >= monto THEN 	  
	       # si el saldo actual de la cuentaA es suficiente para realizar 
           # la transferencia, entonces actualizo el saldo de ambas cuentas 
	         UPDATE caja_ahorro SET saldo = saldo - monto  WHERE nro_ca=cuentaA;
	         UPDATE caja_ahorro SET saldo = saldo + monto  WHERE nro_ca=cuentaB;
			 INSERT INTO transaccion (fecha, hora, monto) VALUES (CURDATE(), CURRENT_TIME(), monto);
			 INSERT INTO transaccion_por_caja VALUES (LAST_INSERT_ID(), nro_caja);
			 INSERT INTO transferencia VALUES (LAST_INSERT_ID(), nro_clienteA, cuentaA, cuentaB);
			 INSERT INTO transaccion (fecha, hora, monto) VALUES (CURDATE(), CURRENT_TIME(), monto);
			 INSERT INTO transaccion_por_caja VALUES (LAST_INSERT_ID(), nro_caja);
			 INSERT INTO deposito VALUES (LAST_INSERT_ID(), cuentaB);
             SELECT 'La transferencia se realizo con exito.' AS resultado;               
	      ELSE  
            SELECT 'Saldo insuficiente para realizar la transferencia.' 
		        AS resultado;
	      END IF;  
	   ELSE  
            SELECT 'Cuenta inexistente.' 
		        AS resultado;  
	   END IF;  	 		
		
	 COMMIT;   # Comete la transacción  
 END; !
 
 CREATE PROCEDURE extraer(IN monto DECIMAL(16,2), IN cuenta INT,
                            IN nro_cliente INT, IN nro_caja INT)
                            
  BEGIN   
     # Declaro una variable local saldo_actual	
	 DECLARE saldo_actual_cuenta DECIMAL(16,2);
     
     # Declaro variables locales para recuperar los errores 
	 DECLARE codigo_SQL  CHAR(5) DEFAULT '00000';	 
	 DECLARE codigo_MYSQL INT DEFAULT 0;
	 DECLARE mensaje_error TEXT;
	 
     DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
	  BEGIN 
		GET DIAGNOSTICS CONDITION 1  codigo_MYSQL= MYSQL_ERRNO,  
		                             codigo_SQL= RETURNED_SQLSTATE, 
									 mensaje_error= MESSAGE_TEXT;
	    SELECT 'SQLEXCEPTION!, Transacción abortada.' AS resultado, 
		        codigo_MySQL, codigo_SQL,  mensaje_error;		
        ROLLBACK;
	  END;		      
         
	 START TRANSACTION;	# Comienza la transacción  
	   IF EXISTS (SELECT * FROM caja_ahorro WHERE nro_ca=cuenta)
	   THEN  # verifico que la cuenta exista
		  SELECT saldo INTO saldo_actual_cuenta
		  FROM caja_ahorro WHERE nro_ca=cuenta FOR UPDATE;
		  # Recupero el saldo de la cuenta en la variable saldo_actual_cuenta.   	    
      
	      IF saldo_actual_cuenta >= monto THEN 	  
	       # si el saldo actual de la cuenta es suficiente para realizar 
           # la extracción, entonces actualizo el saldo de la cuenta
	         UPDATE caja_ahorro SET saldo = saldo - monto  WHERE nro_ca=cuenta;
			 INSERT INTO transaccion (fecha, hora, monto) VALUES (CURDATE(), CURRENT_TIME(), monto);
			 INSERT INTO transaccion_por_caja VALUES (LAST_INSERT_ID(), nro_caja);
			 INSERT INTO extraccion VALUES (LAST_INSERT_ID(), nro_cliente, cuenta);			 
             SELECT 'La extraccion se realizo con exito.' AS resultado;               
	      ELSE  
            SELECT 'Saldo insuficiente para realizar la extraccion.' 
		        AS resultado;
	      END IF;  
	   ELSE  
            SELECT 'Cuenta inexistente.' 
		        AS resultado;  
	   END IF;  	 		
		
	 COMMIT;   # Comete la transacción  
 END; !

    


#------------- Trigger ------------------------


CREATE TRIGGER pago_insert
AFTER INSERT ON prestamo
FOR EACH ROW
BEGIN
	DECLARE nro INT DEFAULT 1;
	WHILE nro <= NEW.cant_meses DO
		INSERT INTO pago (nro_prestamo, nro_pago, fecha_venc) VALUES (NEW.nro_prestamo, nro, date_add(NEW.fecha, interval nro month));
		SET nro = nro + 1;
	END WHILE;
END; !



delimiter ;
 
#------------- Usuarios -----------------------



CREATE USER 'admin'@'localhost'  IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON banco.* TO 'admin'@'localhost' WITH GRANT OPTION;

CREATE USER 'empleado'@'%' IDENTIFIED BY 'empleado';
GRANT SELECT ON banco.empleado TO 'empleado'@'%';
GRANT SELECT ON banco.sucursal TO 'empleado'@'%';
GRANT SELECT ON banco.tasa_plazo_fijo TO 'empleado'@'%';
GRANT SELECT ON banco.tasa_prestamo TO 'empleado'@'%';
GRANT SELECT, INSERT ON banco.prestamo TO 'empleado'@'%';
GRANT SELECT, INSERT ON banco.plazo_fijo TO 'empleado'@'%';
GRANT SELECT, INSERT ON banco.plazo_cliente TO 'empleado'@'%';
GRANT SELECT, INSERT ON banco.caja_ahorro TO 'empleado'@'%';
GRANT SELECT, INSERT ON banco.tarjeta TO 'empleado'@'%';
GRANT SELECT, INSERT, UPDATE ON banco.cliente_ca TO 'empleado'@'%';
GRANT SELECT, INSERT, UPDATE ON banco.cliente TO 'empleado'@'%';
GRANT SELECT, INSERT, UPDATE ON banco.pago TO 'empleado'@'%';

CREATE USER 'atm'@'%' IDENTIFIED BY 'atm';
GRANT SELECT ON banco.trans_cajas_ahorro TO atm@'%';
GRANT SELECT, UPDATE ON banco.tarjeta TO atm@'%';
GRANT EXECUTE ON PROCEDURE banco.transferir TO 'atm'@'%';
GRANT EXECUTE ON PROCEDURE banco.extraer TO 'atm'@'%';